package app.mybad.notifier.ui.screens.mycourses

import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val coursesRepo: CoursesRepo,
    private val medsRepo: MedsRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(MyCoursesState())
    val state get() = _state.asStateFlow()

    init {
        scope.launch {
            coursesRepo.getAllFlow().collect { _state.emit(_state.value.copy(courses = it)) }
        }
        scope.launch {
            medsRepo.getAllFlow().collect { _state.emit(_state.value.copy(meds = it)) }
        }
    }

    fun reduce(intent: MyCoursesIntent) {
        intent
    }

}