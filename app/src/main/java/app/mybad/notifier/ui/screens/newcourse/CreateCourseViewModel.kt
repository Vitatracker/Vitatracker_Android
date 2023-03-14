package app.mybad.notifier.ui.screens.newcourse

import androidx.lifecycle.ViewModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.usecases.courses.CreateCourseUseCase
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
    private val createCourseUseCase: CreateCourseUseCase
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val now = LocalDateTime.now()
    private val _state = MutableStateFlow(
        NewCourseState(
            userId = "userid",
            med = MedDomainModel(id = now.atZone(ZoneId.systemDefault()).toEpochSecond(), userId = "userid"),
            course = CourseDomainModel(
                id = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
                medId = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
                userId = "userid",
                startDate = now.atZone(ZoneId.systemDefault()).withHour(0).withMinute(0)
                    .withSecond(0).toEpochSecond(),
                endDate = now.atZone(ZoneId.systemDefault()).withHour(23).withMinute(59)
                    .withSecond(59).plusMonths(1).toEpochSecond(),
                creationDate = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
            )
        )
    )
    val state get() = _state.asStateFlow()

    fun reduce(intent: NewCourseIntent) {
        when(intent) {
            is NewCourseIntent.Drop -> { scope.launch { _state.emit(NewCourseState()) } }
            is NewCourseIntent.Finish -> {
                scope.launch {
                    createCourseUseCase.execute(
                        med = _state.value.med,
                        course = _state.value.course,
                        usages = _state.value.usages
                    )
                }
            }
            is NewCourseIntent.UpdateMed -> {
                scope.launch { _state.update { it.copy(med = intent.med) } }
            }
            is NewCourseIntent.UpdateCourse -> {
                scope.launch { _state.update { it.copy(course = intent.course) } }
            }
            is NewCourseIntent.UpdateUsages -> {
                scope.launch { _state.update { it.copy(usages = intent.usages) } }
            }
            is NewCourseIntent.UpdateUsagesPattern -> {
                scope.launch {
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
                }
            }
        }
    }

    private fun generateCommonUsages(
        usagesByDay: List<Pair<LocalTime, Int>>,
        now: Long,
        medId: Long,
        userId: String,
        startDate: Long,
        endDate: Long,
        regime: Int,
    ) : List<UsageCommonDomainModel> {
        val startLocalDate = LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.UTC).toLocalDate()
        val endLocalDate = LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.UTC).toLocalDate()
        val interval = ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt().absoluteValue
        return mutableListOf<UsageCommonDomainModel>().apply {
            repeat(interval) { position ->
                if(position % (regime+1) == 0) {
                    usagesByDay.forEach {
                        val time = (it.first.atDate(startLocalDate).plusDays(position.toLong()).atZone(ZoneOffset.systemDefault()).toEpochSecond())
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

