package app.mybad.notifier.ui.screens.newcourse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.start
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.usecases.courses.AddNotificationsUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.CreateUsageUseCase
import app.mybad.domain.usecases.remedies.CreateRemedyUseCase
import app.mybad.notifier.ui.screens.common.generateUsages
import app.mybad.theme.utils.atEndOfDay
import app.mybad.theme.utils.atStartOfDay
import app.mybad.theme.utils.currentDateTime
import app.mybad.theme.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createRemedyUseCase: CreateRemedyUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val createUsageUseCase: CreateUsageUseCase,
    private val addNotifications: AddNotificationsUseCase,
    private val workSync: WorkManager,
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
                        "CreateCourseViewModel::Finish: medId=${
                            _state.value.remedy.id
                        } userId=${_state.value.remedy.userId}"
                    )
                    // записать remedy и получить remedyId
                    createRemedyUseCase(_state.value.remedy).getOrNull()?.let { remedyId ->
                        Log.w(
                            "VTTAG",
                            "CreateCourseViewModel::createRemedyUseCase: remedyId=$remedyId"
                        )
                        _state.update {
                            it.copy(
                                remedy = _state.value.remedy.copy(id = remedyId),
                                course = _state.value.course.copy(remedyId = remedyId),
                            )
                        }
                        // записать course и получить remedyId
                        createCourseUseCase(_state.value.course).getOrNull()?.let { courseId ->
                            Log.w(
                                "VTTAG",
                                "CreateCourseViewModel::createCourseUseCase: courseId=$courseId"
                            )
                            _state.update {
                                it.copy(
                                    course = _state.value.course.copy(id = courseId),
                                    usages = _state.value.usages.map { usage ->
                                        usage.copy(
                                            remedyId = remedyId,
                                            courseId = courseId,
                                        )
                                    },
                                )
                            }
                            createUsageUseCase(_state.value.usages)
                            addNotifications(
                                course = _state.value.course,
                                usages = _state.value.usages,
                            )
                            // синхронизировать
                            workSync.start()
                        } ?: {
                            Log.w("VTTAG", "CreateCourseViewModel::createCourseUseCase: error")
                        }
                    } ?: {
                        Log.w("VTTAG", "CreateCourseViewModel::createRemedyUseCase: error")
                    }
                    _state.emit(newState())
                }

                is NewCourseIntent.UpdateMed -> {
                    _state.update { it.copy(remedy = intent.remedy) }
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
                            "CreateCourseViewModel::UpdateUsagesPattern: remedyId=${
                                _state.value.remedy.id
                            } userId=${_state.value.remedy.userId}"
                        )
                        // записать remedy и получить remedyId
                        createRemedyUseCase(_state.value.remedy).getOrNull()?.let { remedyId ->
                            Log.w(
                                "VTTAG",
                                "CreateCourseViewModel::createRemedyUseCase: remedyId=$remedyId"
                            )
                            _state.update {
                                it.copy(
                                    remedy = _state.value.remedy.copy(id = remedyId),
                                    course = _state.value.course.copy(remedyId = remedyId),
                                )
                            }
                            // записать course и получить courseId
                            createCourseUseCase(_state.value.course).getOrNull()?.let { courseId ->
                                Log.w(
                                    "VTTAG",
                                    "CreateCourseViewModel::createCourseUseCase: courseId=$courseId"
                                )
                                _state.update {
                                    it.copy(
                                        course = _state.value.course.copy(id = courseId),
                                    )
                                }
                                val usages = generateUsages(
                                    usagesByDay = intent.pattern,
                                    remedyId = remedyId,
                                    courseId = courseId,
                                    userId = _state.value.remedy.userId,
                                    startDate = _state.value.course.startDate,
                                    endDate = _state.value.course.endDate,
                                    regime = _state.value.course.regime
                                )
                                createUsageUseCase(usages)
                                Log.w(
                                    "VTTAG",
                                    "CreateCourseViewModel::createUsageUseCase: usages=${usages.size}"
                                )
                                _state.update {
                                    it.copy(
                                        remedy = _state.value.remedy,
                                        course = _state.value.course,
                                        usages = usages,
                                    )
                                }
                                Log.w(
                                    "VTTAG",
                                    "CreateCourseViewModel::UpdateUsagesPattern: remedyId=${
                                        _state.value.remedy.id
                                    } userId=${_state.value.remedy.userId}"
                                )
                            } ?: {
                                Log.w("VTTAG", "CreateCourseViewModel::createCourseUseCase: error")
                            }
                        } ?: {
                            Log.w("VTTAG", "CreateCourseViewModel::createRemedyUseCase: error")
                        }
                    }.invokeOnCompletion {
                        launch {
                            Log.w(
                                "VTTAG",
                                "CreateCourseViewModel::coursesNetworkRepo: userId=${_state.value.remedy.userId}"
                            )
                            //TODO("запустить воркер обновления на беке")
                            _state.emit(newState())
                            // синхронизировать
                            workSync.start()
                        }
                    }
                }
            }
        }
    }

    private fun newState(): NewCourseState {
        val userId = AuthToken.userId
        Log.w("VTTAG", "CreateCourseViewModel::newState: userId=${AuthToken.userId}")
        val currentDateTime = currentDateTime()
        return NewCourseState(
            remedy = RemedyDomainModel(
                createdDate = currentDateTime.toEpochSecond(),
                userId = userId
            ),
            course = CourseDomainModel(
                createdDate = currentDateTime.toEpochSecond(),
                userId = userId,
                startDate = currentDateTime.atStartOfDay().toEpochSecond(),
                endDate = currentDateTime.atEndOfDay().toEpochSecond(),
            )
        )
    }
}
