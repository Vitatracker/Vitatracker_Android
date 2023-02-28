package app.mybad.notifier.ui.screens.calender

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val courses: CoursesRepo,
    private val usages: UsagesRepo,
    private val meds: MedsRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(CalendarState())
    val state get() = _state.asStateFlow()
    init {
        scope.launch {
            courses.getAllFlow().collect {
                _state.emit(_state.value.copy(courses = it))
                Log.w("CVM_", "courses: $it")
            }
        }
        scope.launch {
            meds.getAllFlow().collect {
                _state.emit(_state.value.copy(meds = it))
                Log.w("CVM_", "meds: $it")
            }
        }
        scope.launch {
            usages.getAllFlow().collect {
                _state.emit(_state.value.copy(usages = it))
                Log.w("CVM_", "usages: $it")
            }
        }
    }

    fun reducer(intent: CalendarIntent) {
        when(intent) {
            is CalendarIntent.SetUsage -> {
                Log.w("CVM_", "usage set: ${intent.usageTime} at ${intent.factUsageTime} with med ${intent.medId}")
            }
        }
    }

}