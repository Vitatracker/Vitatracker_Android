package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val loadCoursesUseCase: LoadCoursesUseCase,
) : BaseViewModel<MyCoursesContract.Event, MyCoursesContract.State, MyCoursesContract.Effect>() {


    override fun setInitialState() = MyCoursesContract.State()

    init {
        Log.w("VTTAG", "MyCoursesViewModel: init")
        uploadCoursesInState()
    }

    override fun handleEvents(event: MyCoursesContract.Event) {
        viewModelScope.launch {
            when (event) {
                is MyCoursesContract.Event.CourseEditing -> {
                    setEffect { MyCoursesContract.Effect.Navigation.ToCourseEditing(event.courseId) }
                }

                MyCoursesContract.Event.ActionBack -> setEffect { MyCoursesContract.Effect.Navigation.Back }
            }
        }
    }

    private fun uploadCoursesInState() {
        viewModelScope.launch {
            loadCoursesUseCase().collectLatest { (courses, remedies, usages) ->
                Log.w(
                    "VTTAG",
                    "MyCoursesViewModel::loadCoursesInState: remedies=${remedies.size} courses=${
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
}
