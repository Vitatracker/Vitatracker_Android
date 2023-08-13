package app.mybad.notifier.ui.screens.mycourses

import app.mybad.data.models.EditCourseState
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class MyCoursesContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
        data class Delete(val courseId: Long) : Event
        data class CourseEditing(val courseId: Long) : Event
        data class Update(
            val course: CourseDomainModel,
            val remedy: RemedyDomainModel,
            val usagesPattern: List<UsageFormat>
        ) : Event
    }

    data class State(
        val courses: List<CourseDomainModel> = emptyList(),
        val remedies: List<RemedyDomainModel> = emptyList(),
        val usages: List<UsageDomainModel> = emptyList(),
        val editCourse: EditCourseState = EditCourseState(),
    ) : ViewState

    sealed interface Effect : ViewSideEffect {

        sealed interface Navigation : Effect {
            object Back : Navigation
            data class ToCourseEditing(val courseId: Long) : Navigation
        }
    }
}
