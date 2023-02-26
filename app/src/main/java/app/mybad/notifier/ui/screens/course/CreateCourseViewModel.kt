package app.mybad.notifier.ui.screens.course

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    //courses repo
    //meds repo
    //usages repo
    //common mediator?
) : ViewModel() {

    private val _state = MutableStateFlow(CreateCourseState())
    val state get() = _state.asStateFlow()
    val scope = CoroutineScope(Dispatchers.IO)

    fun reduce(intent: CreateCourseIntent) {
        when(intent) {
            is CreateCourseIntent.Drop -> { scope.launch { _state.emit(CreateCourseState()) } }
            is CreateCourseIntent.Finish -> {
            //TODO: write state fields to repo
                }
            is CreateCourseIntent.NewMed -> {
                scope.launch { _state.emit(_state.value.copy(med = intent.med)) }
                Log.w("CCVM_", "${intent.med}")
            }
            is CreateCourseIntent.NewCourse -> {
                scope.launch { _state.emit(_state.value.copy(course = intent.course)) }
            }
            is CreateCourseIntent.NewUsages -> {
                scope.launch { _state.emit(_state.value.copy(usages = intent.usages)) }
            }
        }
    }
}