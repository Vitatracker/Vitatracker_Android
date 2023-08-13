package app.mybad.notifier.ui.screens.calender

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val loadCoursesUseCase: LoadCoursesUseCase,
    private val updateUsageUseCase: UpdateUsageUseCase,
) : BaseViewModel<CalendarContract.Event, CalendarContract.State, CalendarContract.Effect>() {

    init {
        Log.w("VTTAG", "CalendarViewModel::loadCourses: start")
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch {
            loadCoursesUseCase().collectLatest { (courses, remedies, usages) ->
                Log.w(
                    "VTTAG",
                    "CalendarViewModel::loadCourses: remedies=${remedies.size} courses=${
                        courses.size
                    } usages=${usages.size}"
                )
                setState {
                    copy(
                        courses = courses,
                        remedies = remedies,
                        usages = usages,
                    )
                }
            }
        }
    }

    override fun setInitialState() = CalendarContract.State()

    override fun handleEvents(event: CalendarContract.Event) {
        when (event) {
            is CalendarContract.Event.ActionBack -> {
                setEffect { CalendarContract.Effect.Navigation.Back }
            }

            is CalendarContract.Event.SetUsage -> {
                viewModelScope.launch {
                    updateUsageUseCase(event.usage)
                }
            }
        }
    }

}
