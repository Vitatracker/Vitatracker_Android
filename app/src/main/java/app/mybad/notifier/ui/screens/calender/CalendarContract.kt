package app.mybad.notifier.ui.screens.calender

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import app.mybad.utils.currentDateTime
import kotlinx.datetime.LocalDateTime

class CalendarContract {
    sealed interface Event : ViewEvent {
        data class SetUsage(val usage: UsageDisplayDomainModel) : Event
        data class ChangeDate(val date: LocalDateTime) : Event
        data class SelectDate(val date: LocalDateTime?) : Event
        object ChangeUsagesDaily : Event
        object ActionBack : Event
    }

    data class State(
        val remedies: List<RemedyDomainModel> = emptyList(),
        val courses: List<CourseDomainModel> = emptyList(),
        val patternUsage: List<UsageDisplayDomainModel> = emptyList(),

        val usagesDaily: List<UsageDisplayDomainModel> = emptyList(),

        val date: LocalDateTime = currentDateTime(),
        val selectedDate: LocalDateTime? = null,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object Back : Navigation
        }
    }
}
