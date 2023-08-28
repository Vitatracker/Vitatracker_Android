package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class MyCoursesContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
        data class CourseEditing(val courseId: Long) : Event
    }

    data class State(
        val courses: List<CourseDomainModel> = emptyList(),
        val remedies: List<RemedyDomainModel> = emptyList(),
        val usages: List<UsageDomainModel> = emptyList(),
    ) : ViewState

    sealed interface Effect : ViewSideEffect {

        sealed interface Navigation : Effect {
            object Back : Navigation
            data class ToCourseEditing(val courseId: Long) : Navigation
        }
    }
}
