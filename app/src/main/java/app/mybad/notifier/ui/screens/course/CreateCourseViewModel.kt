package app.mybad.notifier.ui.screens.course

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
class CreateCourseViewModel @Inject constructor(
    private val coursesRepo: CoursesRepo,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo,
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(CreateCourseState())
    val state get() = _state.asStateFlow()

    fun reduce(intent: CreateCourseIntent) {
        when(intent) {
            is CreateCourseIntent.Drop -> { scope.launch { _state.emit(CreateCourseState()) } }
            is CreateCourseIntent.Finish -> {

            }
            is CreateCourseIntent.NewMed -> {
                scope.launch { _state.emit(_state.value.copy(med = intent.med)) }
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