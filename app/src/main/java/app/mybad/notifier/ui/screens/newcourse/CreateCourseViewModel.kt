package app.mybad.notifier.ui.screens.newcourse

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.UsageFormat
import app.mybad.data.models.UsageFormat.Companion.toPattern
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.usecases.notification.AddNotificationsByCourseIdUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.remedies.CreateRemedyUseCase
import app.mybad.domain.usecases.patternusage.CreatePatternUsagesUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTimeSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createRemedyUseCase: CreateRemedyUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val createPatternUsagesUseCase: CreatePatternUsagesUseCase,

    private val addNotifications: AddNotificationsByCourseIdUseCase,
) : BaseViewModel<CreateCourseContract.Event, CreateCourseContract.State, CreateCourseContract.Effect>() {

    init {
        Log.w("VTTAG", "NewCourseNavGraph::CreateCourseViewModel: init")
    }

    override fun setInitialState() = CreateCourseContract.State()

    override fun handleEvents(event: CreateCourseContract.Event) {
        when (event) {
            CreateCourseContract.Event.Drop -> newState()

            CreateCourseContract.Event.Finish -> finishCreation()

            is CreateCourseContract.Event.UpdateRemedy -> setState { copy(remedy = event.remedy) }

            is CreateCourseContract.Event.UpdateCourse -> setState { copy(course = event.course) }

            is CreateCourseContract.Event.UpdateCourseRemindDate -> {
                setState {
                    copy(
                        course = viewState.value.course.copy(
                            remindDate = event.remindDate,
                            interval = event.interval
                        ),
                        courseIntervalEntered = event.remindDate?.let { true } ?: false
                    )
                }
            }

            is CreateCourseContract.Event.UpdateUsages -> setState { copy(usages = event.usages) }

            is CreateCourseContract.Event.UpdateUsagePatterns -> {
                updateStateUsagesPatterns(usagesPattern = event.patterns)
            }

            is CreateCourseContract.Event.DeleteUsagePattern -> {
                updateStateUsagesPatterns(viewState.value.usagesPattern.minus(event.pattern))
            }

            CreateCourseContract.Event.AddUsagesPattern -> addUsagesPattern()

            is CreateCourseContract.Event.ChangeQuantityUsagePattern -> {
                changeQuantityUsagePattern(event.pattern, event.quantity)
            }

            is CreateCourseContract.Event.ChangeTimeUsagePattern -> {
                changeTimeUsagePattern(event.pattern, event.time)
            }

            is CreateCourseContract.Event.UpdateRemedyName -> updateRemedyName(name = event.newName)

            CreateCourseContract.Event.UpdateCourseStartDate -> {
                if (!viewState.value.updateCourseStartDate) setCourseDate()
            }

            is CreateCourseContract.Event.ActionNext -> {
                if (viewState.value.remedy.name.isNullOrBlank()) {
                    setState { copy(isError = true) }
                } else {
                    setEffect { CreateCourseContract.Effect.Navigation.Next }
                }
            }

            CreateCourseContract.Event.ActionBack -> setEffect { CreateCourseContract.Effect.Navigation.Back }

            CreateCourseContract.Event.ActionCollapse -> setEffect { CreateCourseContract.Effect.Collapse }
            CreateCourseContract.Event.ActionExpand -> setEffect { CreateCourseContract.Effect.Expand }
        }
    }

    private fun updateStateUsagesPatterns(usagesPattern: List<UsageFormat>) {
        setState {
            copy(
                usagesPattern = usagesPattern,
                nextAllowed = usagesPattern.isNotEmpty()
            )
        }
    }

    private fun changeTimeUsagePattern(pattern: UsageFormat, time: Int) {
        updateStateUsagesPatterns(
            UsageFormat.changeTimeUsagePattern(
                usagesPattern = viewState.value.usagesPattern,
                pattern = pattern,
                time = time
            )
        )
    }

    private fun changeQuantityUsagePattern(pattern: UsageFormat, quantity: Float) {
        updateStateUsagesPatterns(
            UsageFormat.changeQuantityUsagePattern(
                usagesPattern = viewState.value.usagesPattern,
                pattern = pattern,
                quantity = quantity
            )
        )
    }

    private fun addUsagesPattern() {
        updateStateUsagesPatterns(
            UsageFormat.addUsagesPattern(
                usagesPattern = viewState.value.usagesPattern
            )
        )
    }

    private fun updateRemedyName(name: String) {
        val newRemedy = viewState.value.remedy.copy(name = name)
        setState { copy(remedy = newRemedy, isError = newRemedy.name.isNullOrBlank()) }
    }

    private fun finishCreation() {
        viewModelScope.launch {
            log(
                "finishCreation: remedyId=${
                    viewState.value.remedy.id
                } userId=${viewState.value.remedy.userId}"
            )
            // записать remedy и получить remedyId
            createRemedyUseCase(viewState.value.remedy).onSuccess { remedyId ->
                log("finishCreation: remedyId=$remedyId")
                setState {
                    copy(
                        remedy = viewState.value.remedy.copy(id = remedyId),
                        course = viewState.value.course.copy(remedyId = remedyId),
                    )
                }
                // сформировать паттерн usages
                val patternUsages = viewState.value.usagesPattern.toPattern()
                setState {
                    copy(
                        course = viewState.value.course.copy(patternUsages = patternUsages),
                    )
                }
                // записать course и получить courseId
                createCourseUseCase(viewState.value.course).onSuccess { courseId ->
                    log("finishCreation: courseId=$courseId")
                    setState {
                        copy(
                            course = viewState.value.course.copy(id = courseId),
                        )
                    }
                    log("finishCreation: usages-generate=${viewState.value.usagesPattern.size}")
                    val patterns = viewState.value.usagesPattern.map { pattern ->
                        PatternUsageDomainModel(
                            courseId = courseId,
                            timeInMinutes = pattern.timeInMinutes,
                            quantity = pattern.quantity,
                        )
                    }
                    createPatternUsagesUseCase(patterns).onSuccess {
                        log("finishCreation: addNotifications courseId=${viewState.value.course.id}")
                        // добавляем оповещение
                        addNotifications(courseId = viewState.value.course.id)
                        newState()
                        // синхронизировать
                        AuthToken.requiredSynchronize()
                    }.onFailure {
                        err("finishCreation: error createPatternUsagesUseCase", it)
                    }
                }.onFailure {
                    err("finishCreation: error createCourseUseCase", it)
                }
            }.onFailure {
                err("finishCreation: error createRemedyUseCase", it)
            }
        }
    }

    private fun setCourseDate() {
        val userId = AuthToken.userId
        val date = currentDateTimeSystem()
        log("setCourseDate: userId=$userId date=$date")
        setState {
            copy(
                remedy = viewState.value.remedy.copy(
                    userId = userId
                ),
                course = viewState.value.course.copy(
                    userId = userId,
                    startDate = date.atStartOfDay(),
                    endDate = date.atEndOfDay(),
                ),
                nextAllowed = viewState.value.usagesPattern.isNotEmpty(),
                updateCourseStartDate = true,
//                isError = false,
            )
        }
    }

    private fun newState() {
        setState { setInitialState() }
    }

    override fun onCleared() {
        log("onCleared")
        super.onCleared()
    }

    private fun log(text: String) {
        Log.w("VTTAG", "CreateCourseViewModel::$text")
    }

    private fun err(text: String, e: Throwable) {
        Log.e("VTTAG", "CreateCourseViewModel::$text", e)
    }
}
