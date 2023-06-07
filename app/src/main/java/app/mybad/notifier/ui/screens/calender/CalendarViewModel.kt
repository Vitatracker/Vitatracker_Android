package app.mybad.notifier.ui.screens.calender

import androidx.lifecycle.ViewModel
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.network.repos.repo.CoursesNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val loadCourses: LoadCoursesUseCase,
    private val updateUsage: UpdateUsageUseCase,
    private val coursesNetworkRepo: CoursesNetworkRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(CalendarState())
    val state get() = _state.asStateFlow()
    init {
        scope.launch {
            loadCourses.getCoursesFlow().collect { _state.emit(_state.value.copy(courses = it)) }
        }
        scope.launch {
            loadCourses.getMedsFlow().collect { _state.emit(_state.value.copy(meds = it)) }
        }
        scope.launch {
            loadCourses.getUsagesFlow().collect { _state.emit(_state.value.copy(usages = it)) }
        }
    }

    fun reducer(intent: CalendarIntent) {
        when (intent) {
            is CalendarIntent.SetUsage -> {
                scope.launch {
                    updateUsage.execute(intent.usage)
                    coursesNetworkRepo.updateUsage(intent.usage)
                }
            }
        }
    }
}
