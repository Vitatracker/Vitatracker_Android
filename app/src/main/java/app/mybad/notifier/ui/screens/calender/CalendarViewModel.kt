package app.mybad.notifier.ui.screens.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.data.models.MyCoursesState
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val loadCourses: LoadCoursesUseCase,
    private val updateUsage: UpdateUsageUseCase,
) : ViewModel() {

    val state = loadCourses(AuthToken.userId)
        .mapLatest { (courses, meds, usages) ->
            MyCoursesState(courses = courses, meds = meds, usages = usages)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = MyCoursesState()
        )

    fun reducer(intent: CalendarIntent) {
        when (intent) {
            is CalendarIntent.SetUsage -> {
                viewModelScope.launch {
                    updateUsage.execute(intent.usage)
                }
            }
        }
    }
}
