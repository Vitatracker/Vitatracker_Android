package app.mybad.notifier.ui.screens.mycourses

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.courses.LoadCoursesUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCoursesScreenViewModel @Inject constructor(
    loadCourses: LoadCoursesUseCase
) : BaseViewModel<MyCoursesScreenContract.Event, MyCoursesScreenContract.State, MyCoursesScreenContract.Effect>() {

    init {
        viewModelScope.launch {
            loadCourses(AuthToken.userId).collectLatest { (courses, meds, usages) ->
                Log.w("VTTAG", "MyCoursesScreenViewModel::state: meds=${meds.size} usages=${usages.size}")
                val result = mutableListOf<CoursePresenterItem>()
                meds.forEach { med ->
                    val foundCourse = courses.find { it.medId == med.id }
                    val foundUsages = usages.filter { it.medId == med.id }
                    if (foundCourse != null) {
                        result.add(
                            CoursePresenterItem(
                                med,
                                foundCourse,
                                foundUsages
                            )
                        )
                    }
                }
                viewModelScope.launch(Dispatchers.Main) {
                    setState { copy(courseItems = result) }
                }
            }
        }
    }

    override fun setInitialState(): MyCoursesScreenContract.State {
        return MyCoursesScreenContract.State(
            courseItems = emptyList()
        )
    }

    override fun handleEvents(event: MyCoursesScreenContract.Event) {
        when (event) {
            is MyCoursesScreenContract.Event.EditCourseClicked -> {
                setEffect { MyCoursesScreenContract.Effect.Navigation.ToEditCourse(event.courseId) }
            }
        }
    }

}