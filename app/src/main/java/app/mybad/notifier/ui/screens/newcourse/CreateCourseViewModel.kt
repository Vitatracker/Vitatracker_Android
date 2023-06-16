package app.mybad.notifier.ui.screens.newcourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.network.repos.repo.CoursesNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.*
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createCourseUseCase: CreateCourseUseCase,
    private val coursesNetworkRepo: CoursesNetworkRepo
) : ViewModel() {

    private val _state = MutableStateFlow(newState())
    val state = _state.asStateFlow()

    fun reduce(intent: NewCourseIntent) {
        viewModelScope.launch {
            when (intent) {
                is NewCourseIntent.Drop -> {
                    _state.emit(newState())
                }

                is NewCourseIntent.Finish -> {
                        createCourseUseCase.execute(
                            med = _state.value.med,
                            course = _state.value.course,
                            usages = _state.value.usages
                        )
                        _state.emit(newState())
                }

                is NewCourseIntent.UpdateMed -> {
                    _state.update { it.copy(med = intent.med) }
                }

                is NewCourseIntent.UpdateCourse -> {
                     _state.update { it.copy(course = intent.course) }
                }

                is NewCourseIntent.UpdateUsages -> {
                    _state.update { it.copy(usages = intent.usages) }
                }

                is NewCourseIntent.UpdateUsagesPattern -> {
                    launch {
                        val usages = generateCommonUsages(
                            usagesByDay = intent.pattern,
                            now = Instant.now().epochSecond,
                            medId = _state.value.med.id,
                            userId = _state.value.userId,
                            startDate = _state.value.course.startDate,
                            endDate = _state.value.course.endDate,
                            regime = _state.value.course.regime
                        )
                        _state.update { it.copy(usages = usages) }
                        createCourseUseCase.execute(
                            med = _state.value.med,
                            course = _state.value.course,
                            usages = _state.value.usages
                        )
                    }.invokeOnCompletion {
                        launch {
                            coursesNetworkRepo.updateAll(
                                med = _state.value.med,
                                course = _state.value.course,
                                usages = _state.value.usages
                            )
                            //TODO("проверить тут userId")
                            _state.emit(newState())
//                            _state.emit(newState(_state.value.course.userId))
                        }
                    }
                }
            }
        }
    }

    private fun generateCommonUsages(
        usagesByDay: List<Pair<LocalTime, Int>>,
        now: Long,
        medId: Long,
        userId: Long,
        startDate: Long,
        endDate: Long,
        regime: Int,
    ): List<UsageCommonDomainModel> {
        val startLocalDate = LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.UTC).toLocalDate()
        val endLocalDate = LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.UTC).toLocalDate()
        val interval = ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt().absoluteValue
        return mutableListOf<UsageCommonDomainModel>().apply {
            repeat(interval) { position ->
                if (position % (regime + 1) == 0) {
                    usagesByDay.forEach {
                        val time = (
                            it.first.atDate(startLocalDate).plusDays(position.toLong()).atZone(
                                ZoneOffset.systemDefault()
                            ).toEpochSecond()
                            )
                        if (time > now) {
                            this.add(
                                UsageCommonDomainModel(
                                    medId = medId,
                                    userId = userId,
                                    creationTime = now,
                                    useTime = time,
                                    quantity = it.second
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun newState(userid: Long = -1L): NewCourseState {
        val id = userid.takeIf { it != -1L } ?: AuthToken.userId
        val now = LocalDateTime.now()
        return NewCourseState(
            userId = id,
            med = MedDomainModel(
                id = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
                userId = id
            ),
            course = CourseDomainModel(
                id = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
                medId = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
                userId = id,
                startDate = now.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0)
                    .withSecond(0).toEpochSecond(),
                endDate = now.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59)
                    .withSecond(59).plusMonths(1).toEpochSecond(),
                creationDate = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
            )
        )
    }
}
