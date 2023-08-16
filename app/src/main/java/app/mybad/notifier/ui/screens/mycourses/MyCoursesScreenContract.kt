package app.mybad.notifier.ui.screens.mycourses

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class MyCoursesScreenContract {
    sealed class Event : ViewEvent {
        data class EditCourseClicked(val courseId: Long) : Event()
    }

    data class State(val courseItems: List<CoursePresenterItem>) : ViewState

    sealed class Effect : ViewSideEffect {

        sealed class Navigation : Effect() {
            data class ToEditCourse(val courseId: Long) : Navigation()
        }
    }
}