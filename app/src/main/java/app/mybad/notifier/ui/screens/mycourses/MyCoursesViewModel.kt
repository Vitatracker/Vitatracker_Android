package app.mybad.notifier.ui.screens.mycourses

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(MyCoursesState())
    val state get() = _state.asStateFlow()

    fun reduce(intent: MyCoursesIntent) {

    }

}