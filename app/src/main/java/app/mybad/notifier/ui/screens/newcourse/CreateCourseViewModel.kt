package app.mybad.notifier.ui.screens.newcourse

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.UsageFormat
import app.mybad.data.models.UsageFormat.Companion.toPattern
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.notification.AddNotificationsByCourseIdUseCase
import app.mybad.domain.usecases.patternusage.CreatePatternUsagesUseCase
import app.mybad.domain.usecases.remedies.CreateRemedyUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTimeSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createRemedyUseCase: CreateRemedyUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val createPatternUsagesUseCase: CreatePatternUsagesUseCase,

    private val addNotifications: AddNotificationsByCourseIdUseCase,
) : BaseViewModel<CreateCourseContract.Event, CreateCourseContract.State, CreateCourseContract.Effect>() {

    private var confirmBack = false

    init {
        Log.w("VTTAG", "NewCourseNavGraph::CreateCourseViewModel: init")
    }

    override fun setInitialState() = CreateCourseContract.State()

    override fun handleEvents(event: CreateCourseContract.Event) {
        when (event) {
            CreateCourseContract.Event.Drop -> newState()

            CreateCourseContract.Event.Finish -> finishCreation()

            is CreateCourseContract.Event.UpdateRemedy -> setState { copy(remedy = event.remedy) }

            is CreateCourseContract.Event.UpdateCourse -> updateCourse(event.date, event.regime)

            is CreateCourseContract.Event.UpdateCourseRemindDate -> {
                updateCourseRemindDate(event.remindDate, event.interval)
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

            is CreateCourseContract.Event.UpdateRemedyName -> updateRemedyName(event.value)
            is CreateCourseContract.Event.UpdateRemedyType -> updateRemedyType(event.value)
            is CreateCourseContract.Event.UpdateRemedyUnit -> updateRemedyUnit(event.value)
            is CreateCourseContract.Event.UpdateRemedyRelations -> updateRemedyRelations(event.value)

            CreateCourseContract.Event.UpdateCourseStartDate -> {
                if (!viewState.value.updateCourseStartDate) setCourseDate()
            }

            is CreateCourseContract.Event.ActionNext -> next()

            CreateCourseContract.Event.ActionBack -> {
                if (confirmBack) showConfirm(true) else back()
            }

            is CreateCourseContract.Event.ConfirmBack -> confirmBack = event.confirm

            CreateCourseContract.Event.Cancel -> showConfirm(false)

            CreateCourseContract.Event.ActionCollapse -> setEffect { CreateCourseContract.Effect.Collapse }

            CreateCourseContract.Event.ActionExpand -> setEffect { CreateCourseContract.Effect.Expand }
        }
    }

    private fun updateCourse(date: Pair<LocalDateTime, LocalDateTime>?, regime: Int?) {
        date?.let {
            setState {
                copy(course = course.copy(startDate = date.first, endDate = date.second))
            }
        }
        regime?.let {
            setState {
                copy(course = course.copy(regime = regime))
            }
        }
    }

    private fun updateCourseRemindDate(remindDate: LocalDateTime?, interval: Long) {
        setState {
            copy(
                course = viewState.value.course.copy(
                    remindDate = remindDate,
                    interval = interval
                ),
                courseIntervalEntered = remindDate?.let { true } ?: false
            )
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

    private fun updateRemedyType(value: Int) {
        val newRemedy = viewState.value.remedy.copy(type = value)
        setState { copy(remedy = newRemedy, isError = newRemedy.name.isNullOrBlank()) }
    }

    private fun updateRemedyUnit(value: Int) {
        val newRemedy = viewState.value.remedy.copy(measureUnit = value)
        setState { copy(remedy = newRemedy, isError = newRemedy.name.isNullOrBlank()) }
    }

    private fun updateRemedyRelations(value: Int) {
        val newRemedy = viewState.value.remedy.copy(beforeFood = value)
        setState { copy(remedy = newRemedy, isError = newRemedy.name.isNullOrBlank()) }
    }

    private fun finishCreation() {
        viewModelScope.launch {
            log(
                "finishCreation: remedyId=${
                    viewState.value.remedy.id
                } userId=${viewState.value.remedy.userId}"
            )
            // отобразить лоадер
            launch {
                setState {
                    copy(loader = true)
                }
            }
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
//                        newState()
                        // синхронизировать
                        AuthToken.requiredSynchronize()
                        //
                        next()
                    }.onFailure {
                        log("finishCreation: error createPatternUsagesUseCase", it)
                        error(it.localizedMessage)
                    }
                }.onFailure {
                    log("finishCreation: error createCourseUseCase", it)
                    error(it.localizedMessage)
                }
            }.onFailure {
                log("finishCreation: error createRemedyUseCase", it)
                error(it.localizedMessage)
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

    private fun cancel() {
        setState { copy(confirmBack = false) }
    }

    private fun showConfirm(show: Boolean) {
        setState { copy(confirmBack = show) }
    }

    private fun back() {
        setState { copy(confirmBack = false) }
        setEffect { CreateCourseContract.Effect.Navigation.Back }
    }

    private fun next() {
        if (viewState.value.remedy.name.isNullOrBlank()) {
            setState { copy(isError = true) }
        } else {
            setEffect { CreateCourseContract.Effect.Navigation.Next }
        }
    }

    private fun error(message: String?) {
        setState {
            copy(loader = false)
        }
        message?.let {
            setEffect { CreateCourseContract.Effect.Toast(message) }
        }
    }

    private fun log(text: String, e: Throwable? = null) {
        if (e == null) Log.w("VTTAG", "CreateCourseViewModel::$text")
        else Log.e("VTTAG", "CreateCourseViewModel::$text", e)
    }
}
