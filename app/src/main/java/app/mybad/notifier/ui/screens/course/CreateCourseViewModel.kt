package app.mybad.notifier.ui.screens.course

import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
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
    private val notificationsScheduler: NotificationsScheduler
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(CreateCourseState())
    val state get() = _state.asStateFlow()

    fun reduce(intent: CreateCourseIntent) {
        when(intent) {
            is CreateCourseIntent.Drop -> { scope.launch { _state.emit(CreateCourseState()) } }
            is CreateCourseIntent.Finish -> {
                scope.launch {
                    coursesRepo.add(_state.value.course)
                    medsRepo.add(_state.value.med)
                    usagesRepo.addUsages(_state.value.usages)
                    notificationsScheduler.add(_state.value.usages)
                }
            }
            is CreateCourseIntent.NewMed -> {
                scope.launch { _state.emit(_state.value.copy(med = intent.med)) }
            }
            is CreateCourseIntent.NewCourse -> {
                scope.launch { _state.emit(_state.value.copy(course = intent.course)) }
            }
            is CreateCourseIntent.NewUsages -> {
                scope.launch {
                    _state.emit(_state.value.copy(usages = intent.usages))
                }
            }
        }
    }
}

