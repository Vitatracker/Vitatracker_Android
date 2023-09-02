package app.mybad.notifier.ui.screens.mycoursesedit

import app.mybad.data.models.DateCourseLimit
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import kotlinx.datetime.DateTimePeriod

class MyCoursesEditContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
        data class Delete(val courseId: Long) : Event

        object AddUsagesPattern : Event
        data class DeleteUsagePattern(val pattern: UsageFormat) : Event
        data class ChangeQuantityUsagePattern(val pattern: UsageFormat, val quantity: Int) : Event
        data class ChangeTimeUsagePattern(val pattern: UsageFormat, val time: Int) : Event

        data class UpdateAndEnd(
            val course: CourseDomainModel,
            val remedy: RemedyDomainModel,
        ) : Event

        object NotificationEditing : Event
        data class NotificationUpdateAndEnd(
            val remindTime: Int,
            val coursesInterval: DateTimePeriod,
            val remindBeforePeriod: DateTimePeriod,
        ) : Event
    }

    data class State(
        val course: CourseDomainModel = CourseDomainModel(),
        val remedy: RemedyDomainModel = RemedyDomainModel(),
        val usagesPattern: List<UsageFormat> = emptyList(),

        val usagesPatternEdit: List<UsageFormat> = emptyList(),
        val remindTime: Int = 0,// тут только время в минутах
        val coursesInterval: DateTimePeriod = DateTimePeriod(days = 3),
        val remindBeforePeriod: DateTimePeriod = DateTimePeriod(days = 3),

        val nextAllowed: Boolean = false,
        val dateLimit: DateCourseLimit,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {

        sealed interface Navigation : Effect {
            object Back : Navigation
            object NotificationEditing : Navigation
        }
    }
}
