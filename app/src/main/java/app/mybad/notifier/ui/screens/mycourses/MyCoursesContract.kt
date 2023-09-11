package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class MyCoursesContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
        data class CourseEditing(val courseId: Long) : Event
    }

    data class State(
        val courses: List<CourseDisplayDomainModel> = emptyList(),
    ) : ViewState

    sealed interface Effect : ViewSideEffect {

        sealed interface Navigation : Effect {
            object Back : Navigation
            data class ToCourseEditing(val courseId: Long) : Navigation
        }
    }
}
