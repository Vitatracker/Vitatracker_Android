package app.mybad.notifier.ui.screens.calender

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class CalendarContract {
    sealed interface Event : ViewEvent {
        data class SetUsage(val usage: UsageDomainModel) : Event
        object ActionBack : Event
    }

    data class State(
        val remedies: List<RemedyDomainModel> = emptyList(),
        val courses: List<CourseDomainModel> = emptyList(),
        val usages: List<UsageDomainModel> = emptyList(),
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object Back : Navigation
        }
    }
}
