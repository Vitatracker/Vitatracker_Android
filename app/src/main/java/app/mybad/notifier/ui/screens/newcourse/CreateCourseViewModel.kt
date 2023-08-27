package app.mybad.notifier.ui.screens.newcourse

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.usecases.courses.AddNotificationsUseCase
import app.mybad.domain.usecases.courses.CreateCourseUseCase
import app.mybad.domain.usecases.courses.CreateUsagesUseCase
import app.mybad.domain.usecases.meds.CreateMedUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.ui.screens.common.generateCommonUsages
import app.mybad.notifier.utils.atEndOfDay
import app.mybad.notifier.utils.atStartOfDay
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.getCurrentDateTimeWithoutSecond
import app.mybad.notifier.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createMed: CreateMedUseCase,
    private val createCourse: CreateCourseUseCase,
    private val createUsages: CreateUsagesUseCase,
    private val addNotifications: AddNotificationsUseCase
) : BaseViewModel<CreateCourseScreensContract.Event, CreateCourseScreensContract.State, CreateCourseScreensContract.Effect>() {

    override fun setInitialState(): CreateCourseScreensContract.State {
        return newState()
    }

    override fun handleEvents(event: CreateCourseScreensContract.Event) {
        when (event) {
            CreateCourseScreensContract.Event.Finish -> {
                viewModelScope.launch {
                    actionFinish()
                }
            }

            is CreateCourseScreensContract.Event.UpdateCourse -> {
                setState { copy(course = event.course) }
            }

            is CreateCourseScreensContract.Event.UpdateMed -> {
                setState { copy(med = event.med) }
            }

            is CreateCourseScreensContract.Event.UpdateUsages -> {
                setState { copy(usages = event.usages) }
            }

            CreateCourseScreensContract.Event.ActionBack -> setEffect {
                CreateCourseScreensContract.Effect.Navigation.ActionBack
            }

            is CreateCourseScreensContract.Event.ActionNext -> {
                val currentState = viewState.value
                if (currentState.med.name.isNullOrBlank()) {
                    setState { copy(isError = true) }
                } else if (currentState.course.startDate >= currentState.course.endDate) {
                    setState { copy(isError = true) }
                } else {
                    setEffect {
                        CreateCourseScreensContract.Effect.Navigation.ActionNext
                    }
                }
            }

            is CreateCourseScreensContract.Event.UpdateMedName -> {
                val newMed = viewState.value.med.copy(name = event.newName)
                setState { copy(med = newMed, isError = newMed.name.isNullOrBlank()) }
            }

            CreateCourseScreensContract.Event.CourseIntervalEntered -> {
                setState { copy(courseIntervalEntered = true) }
            }

            is CreateCourseScreensContract.Event.AddUsagesPattern -> {
                addNewPattern()
            }

            is CreateCourseScreensContract.Event.RemoveUsagesPattern -> {
                val currentUsagesPatterns = viewState.value.usagesPattern.toMutableList().apply {
                    removeAt(event.index)
                }
                setState { copy(usagesPattern = currentUsagesPatterns, nextAllowed = currentUsagesPatterns.isNotEmpty()) }
            }

            is CreateCourseScreensContract.Event.UpdateUsagePattern -> {
                updatePattern(event.index, event.pattern)
            }
        }
    }

    private fun addNewPattern() {
        val currentPatterns = viewState.value.usagesPattern.toMutableList()
        val patternDate = if (currentPatterns.isEmpty()){
            getCurrentDateTimeWithoutSecond().toEpochSecond()
        } else {
            currentPatterns.last().first
        }
        val currentUsagesPatterns = currentPatterns.apply {
            add(Pair(patternDate, 1))
            sortBy { it.first }
        }
        setState { copy(usagesPattern = currentUsagesPatterns, nextAllowed = true) }
    }

    private fun updatePattern(index: Int, pattern: Pair<Long, Int>) {
        val currentUsagesPatterns = viewState.value.usagesPattern.toMutableList()
        val foundItem = currentUsagesPatterns[index]
        currentUsagesPatterns.replaceAll { oldItem ->
            if (oldItem == foundItem) {
                oldItem.copy(first = pattern.first, second = pattern.second)
            } else
                oldItem
        }
        currentUsagesPatterns.sortBy { it.first }
        setState { copy(usagesPattern = currentUsagesPatterns) }
    }

    private suspend fun actionFinish() {
        Log.w(
            "VTTAG",
            "CreateCourseViewModel::Finish: medId=${viewState.value.med.id} userId=${viewState.value.med.userId}"
        )

        // записать med и получить medId
        val medId = createMed(viewState.value.med)
        setState {
            copy(
                med = viewState.value.med.copy(id = medId),
                course = viewState.value.course.copy(medId = medId),
            )
        }

        // записать course и получить medId
        val courseId = createCourse(viewState.value.course)
        setState {
            copy(
                course = viewState.value.course.copy(id = courseId)
            )
        }
        setState {
            copy(
                usages = viewState.value.usages.map { usages ->
                    usages.copy(medId = medId)
                },
            )
        }
        val currentState = viewState.value
        val usages = generateCommonUsages(
            usagesByDay = currentState.usagesPattern,
            medId = medId,
            userId = currentState.med.userId,
            startDate = currentState.course.startDate,
            endDate = currentState.course.endDate,
            regime = currentState.course.regime
        )
        createUsages(usages)
        addNotifications(
            course = currentState.course,
            usages = usages,
        )
    }

    private fun newState(): CreateCourseScreensContract.State {
        val userId = AuthToken.userId
        Log.w("VTTAG", "CreateCourseViewModel::newState: userId=$userId")
        val currentDateTime = getCurrentDateTime()
        return CreateCourseScreensContract.State(
            med = MedDomainModel(
                creationDate = currentDateTime.toEpochSecond(),
                userId = userId
            ),
            course = CourseDomainModel(
                creationDate = currentDateTime.toEpochSecond(),
                userId = userId,
                startDate = currentDateTime.atStartOfDay().toEpochSecond(),
                endDate = currentDateTime.atEndOfDay().toEpochSecond(),
                regime = 0
            )
        )
    }
}
