package app.mybad.notifier.ui.screens.main

import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import app.mybad.utils.currentDateTime
import kotlinx.datetime.LocalDateTime

class MainContract {
    sealed interface Event : ViewEvent {
        data class ChangeDate(val date: LocalDateTime) : Event
        data class SetUsageFactTime(val usageId: Long) : Event
    }

    data class State(
        val remedies: List<RemedyDomainModel> = emptyList(),
        val usages: List<UsageDomainModel> = emptyList(),
        val date: LocalDateTime = currentDateTime()
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        object Navigation : Effect
    }
}
