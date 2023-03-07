package app.mybad.notifier.ui.screens.mycourses

import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val coursesRepo: CoursesRepo,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(MyCoursesState())
    val state get() = _state.asStateFlow()

    init {
        scope.launch {
            coursesRepo.getAllFlow().collect { courses -> _state.update { it.copy(courses = courses) } }
        }
        scope.launch {
            medsRepo.getAllFlow().collect { meds -> _state.update { it.copy(meds = meds) } }
        }
        scope.launch {
            usagesRepo.getCommonAllFlow().collect { usages -> _state.update { it.copy(usages = usages) } }
        }
    }

    fun reduce(intent: MyCoursesIntent) {
        intent
    }

}