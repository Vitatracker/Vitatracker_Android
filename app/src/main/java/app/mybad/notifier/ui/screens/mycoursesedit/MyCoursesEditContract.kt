package app.mybad.notifier.ui.screens.mycoursesedit

import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class MyCoursesEditContract {
    sealed interface Event : ViewEvent {
        data object ActionBack : Event
        data class Delete(val courseId: Long) : Event
        data object ConfirmationDelete : Event
        data object CancelDelete : Event
        data object AddUsagesPattern : Event
        data class DeleteUsagePattern(val pattern: UsageFormat) : Event
        data class ChangeQuantityUsagePattern(val pattern: UsageFormat, val quantity: Float) : Event
        data class ChangeTimeUsagePattern(val pattern: UsageFormat, val time: Int) : Event

        data class Update(
            val course: CourseDomainModel,
            val remedy: RemedyDomainModel,
        ) : Event

        data object Save : Event
        data object NotificationEditing : Event
        data class NotificationUpdateAndEnd(
            val remindTime: Int,// тут в минутах HH:mm
            val coursesInterval: Int, // дней
            val remindBeforePeriod: Int,
        ) : Event
    }

    data class State(
        val course: CourseDomainModel = CourseDomainModel(),
        val remedy: RemedyDomainModel = RemedyDomainModel(),

        val usagesPattern: List<UsageFormat> = emptyList(),
        val usagesPatternEdit: List<UsageFormat> = emptyList(),

        val remindTime: Int = 0,// тут обязательно 00:00, иначе будет создаваться новый курс
        val coursesInterval: Int = 0,
        val remindBeforePeriod: Int = 0,

        val nextAllowed: Boolean = false,
        val confirmation: Boolean = false,
        val loader: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object Back : Navigation
            data object NotificationEditing : Navigation
        }
    }
}
