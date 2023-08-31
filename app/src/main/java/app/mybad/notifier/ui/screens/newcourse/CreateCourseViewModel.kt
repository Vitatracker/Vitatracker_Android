package app.mybad.notifier.ui.screens.newcourse

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.DateCourseLimit
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.usecases.courses.AddNotificationsUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.SynchronizationCourseUseCase
import app.mybad.domain.usecases.remedies.CreateRemedyUseCase
import app.mybad.domain.usecases.usages.CreatePatternUsagesUseCase
import app.mybad.domain.usecases.usages.CreateUsagesUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.ui.common.generateUsages
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTimeInSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createRemedyUseCase: CreateRemedyUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val createUsagesUseCase: CreateUsagesUseCase,
    private val createPatternUsagesUseCase: CreatePatternUsagesUseCase,

    private val addNotifications: AddNotificationsUseCase,

    private val synchronizationCourseUseCase: SynchronizationCourseUseCase,
) : BaseViewModel<CreateCourseContract.Event, CreateCourseContract.State, CreateCourseContract.Effect>() {

    init {
        Log.w("VTTAG", "NewCourseNavGraph::CreateCourseViewModel: init")
    }

    override fun setInitialState() = CreateCourseContract.State(
        dateLimit = DateCourseLimit(currentDateTimeInSecond())
    )

    override fun handleEvents(event: CreateCourseContract.Event) {
        when (event) {
            CreateCourseContract.Event.Drop -> newState()

            CreateCourseContract.Event.Finish -> finishCreation()

            is CreateCourseContract.Event.UpdateRemedy -> setState { copy(remedy = event.remedy) }

            is CreateCourseContract.Event.UpdateCourse -> setState { copy(course = event.course) }

            is CreateCourseContract.Event.UpdateUsages -> setState { copy(usages = event.usages) }

            is CreateCourseContract.Event.UpdateUsagePatterns -> setState { copy(usagesPattern = event.patterns) }

            is CreateCourseContract.Event.DeleteUsagePattern -> setState {
                copy(usagesPattern = usagesPattern.minus(event.pattern))
            }

            CreateCourseContract.Event.AddUsagesPattern -> addUsagesPattern()

            is CreateCourseContract.Event.ChangeQuantityUsagePattern -> {
                changeQuantityUsagePattern(event.pattern, event.quantity)
            }

            is CreateCourseContract.Event.ChangeTimeUsagePattern -> {
                changeTimeUsagePattern(event.pattern, event.time)
            }

            is CreateCourseContract.Event.UpdateRemedyName -> updateRemedyName(name = event.newName)

            CreateCourseContract.Event.CourseIntervalEntered -> {
                setState { copy(courseIntervalEntered = true) }
            }

            CreateCourseContract.Event.UpdateCourseStartDateAndLimit -> {
                if (viewState.value.course.startDate <= 0L) setCourseDate()
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
        setState { copy(usagesPattern = usagesPattern, nextAllowed = usagesPattern.isNotEmpty()) }
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

    private fun changeQuantityUsagePattern(pattern: UsageFormat, quantity: Int) {
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
                            remedyId = remedyId,
                            courseId = courseId,
                            timeInMinutes = pattern.timeInMinutes,
                            quantity = pattern.quantity,
                        )
                    }
                    createPatternUsagesUseCase(patterns).onFailure {
                        err("finishCreation: error createPatternUsagesUseCase", it)
                    }
                    val usages = generateUsages(
                        usagesByDay = viewState.value.usagesPattern,
                        remedyId = remedyId,
                        courseId = courseId,
                        userId = viewState.value.course.userId,
                        startDate = viewState.value.course.startDate,
                        endDate = viewState.value.course.endDate,
                        regime = viewState.value.course.regime
                    )
                    log("finishCreation: usages=${usages.size}")
                    createUsagesUseCase(usages).onSuccess {
                        log("finishCreation: usages-ok")
                        setState {
                            copy(
                                usages = usages,
                            )
                        }
                        // добавляем оповещение
                        addNotifications(
                            course = viewState.value.course,
                            usages = viewState.value.usages,
                        )
                        newState()
                        log("finishCreation: synchronizationCourseUseCase")
                        // синхронизировать
                        synchronizationCourseUseCase(currentDateTimeInSecond()).onFailure {
                            err("finishCreation: error synchronizationCourseUseCase", it)
                        }
                    }.onFailure {
                        err("finishCreation: error createUsagesUseCase", it)
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
        val date = currentDateTimeInSecond()
        log("setCourseDate: userId=$userId date=$date")
        setState {
            copy(
                dateLimit = DateCourseLimit(date),
                remedy = viewState.value.remedy.copy(
                    createdDate = date,
                    userId = userId
                ),
                course = viewState.value.course.copy(
                    createdDate = date,
                    userId = userId,
                    startDate = date.atStartOfDay(),
                    endDate = date.atEndOfDay(),
                ),
                isError = false,
            )
        }
    }

    private fun newState() {
        setState { setInitialState() }
    }

    private fun log(text: String) {
        Log.w("VTTAG", "CreateCourseViewModel::$text")
    }

    private fun err(text: String, e: Throwable) {
        Log.e("VTTAG", "CreateCourseViewModel::$text", e)
    }
}
