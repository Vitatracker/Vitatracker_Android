package app.mybad.notifier.ui.screens.newcourse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.usecases.courses.AddNotificationsUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.CreateUsagesUseCase
import app.mybad.domain.usecases.courses.UpdateCourseAllUseCase
import app.mybad.domain.usecases.meds.CreateMedUseCase
import app.mybad.notifier.ui.screens.common.generateCommonUsages
import app.mybad.notifier.utils.atEndOfDay
import app.mybad.notifier.utils.atStartOfDay
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createMed: CreateMedUseCase,
    private val createCourse: CreateCourseUseCase,
    private val createUsages: CreateUsagesUseCase,
    private val addNotifications: AddNotificationsUseCase,
    private val updateCourseAll: UpdateCourseAllUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(newState())
    val state = _state.asStateFlow()

    fun reduce(intent: NewCourseIntent) {
        Log.w("VTTAG", "CreateCourseViewModel::reduce: in")
        viewModelScope.launch {
            when (intent) {
                is NewCourseIntent.Drop -> {
                    Log.w("VTTAG", "CreateCourseViewModel::reduce: Drop")
                    _state.emit(newState())
                }

                is NewCourseIntent.Finish -> {
                    Log.w(
                        "VTTAG",
                        "CreateCourseViewModel::Finish: medId=${_state.value.med.id} userId=${_state.value.med.userId}"
                    )
                    // записать med и получить medId
                    val medId = createMed(_state.value.med)
                    _state.update {
                        it.copy(
                            med = _state.value.med.copy(id = medId),
                            course = _state.value.course.copy(medId = medId),
                        )
                    }
                    // записать course и получить medId
                    val courseId = createCourse(_state.value.course)
                    _state.update {
                        it.copy(
                            course = _state.value.course.copy(id = courseId),
                        )
                    }
                    _state.update {
                        it.copy(
                            usages = _state.value.usages.map { usages ->
                                usages.copy(medId = medId)
                            },
                        )
                    }
                    createUsages(_state.value.usages)
                    addNotifications(
                        course = _state.value.course,
                        usages = _state.value.usages,
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
                        Log.w(
                            "VTTAG",
                            "CreateCourseViewModel::UpdateUsagesPattern: medId=${_state.value.med.id} userId=${_state.value.med.userId}"
                        )
                        // записать med и получить medId
                        val medId = createMed(_state.value.med)
                        _state.update {
                            it.copy(
                                med = _state.value.med.copy(id = medId),
                                course = _state.value.course.copy(medId = medId),
                            )
                        }
                        // записать course и получить medId
                        val courseId = createCourse(_state.value.course)
                        _state.update {
                            it.copy(
                                course = _state.value.course.copy(id = courseId),
                            )
                        }
                        val usages = generateCommonUsages(
                            usagesByDay = intent.pattern,
                            medId = medId,
                            userId = _state.value.med.userId,
                            startDate = _state.value.course.startDate,
                            endDate = _state.value.course.endDate,
                            regime = _state.value.course.regime
                        )
                        createUsages(usages)
                        _state.update {
                            it.copy(
                                med = _state.value.med,
                                course = _state.value.course,
                                usages = usages,
                            )
                        }
                        Log.w(
                            "VTTAG",
                            "CreateCourseViewModel::UpdateUsagesPattern: medId=${_state.value.med.id} userId=${_state.value.med.userId}"
                        )
                    }.invokeOnCompletion {
                        launch {
                            Log.w(
                                "VTTAG",
                                "CreateCourseViewModel::coursesNetworkRepo: userId=${_state.value.med.userId}"
                            )
                            updateCourseAll(
                                med = _state.value.med,
                                course = _state.value.course,
                                usages = _state.value.usages
                            )
                            _state.emit(newState())
                        }
                    }
                }
            }
        }
    }

    private fun newState(): NewCourseState {
        val userId = AuthToken.userId
        Log.w("VTTAG", "CreateCourseViewModel::newState: userId=${AuthToken.userId}")
        val currentDateTime = getCurrentDateTime()
        return NewCourseState(
//            userId = userId,
            med = MedDomainModel(
                creationDate = currentDateTime.toEpochSecond(),
                userId = userId
            ),
            course = CourseDomainModel(
                creationDate = currentDateTime.toEpochSecond(),
                userId = userId,
                startDate = currentDateTime.atStartOfDay().toEpochSecond(),
                endDate = currentDateTime.atEndOfDay().toEpochSecond(),
            )
        )
    }
}
