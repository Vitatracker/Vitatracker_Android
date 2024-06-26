package app.mybad.notifier.ui.screens.main

import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import app.mybad.utils.atNoonOfDay
import app.mybad.utils.currentDateTimeSystem
import kotlinx.datetime.LocalDateTime

class MainContract {
    sealed interface Event : ViewEvent {
        data class ChangeDate(val date: LocalDateTime) : Event
        data class SetUsageFactTime(val usageKey: String) : Event
    }

    data class State(
        val patternsAndUsages: Map<String, UsageDisplayDomainModel> = emptyMap(),

        val selectDate: LocalDateTime = currentDateTimeSystem().atNoonOfDay(),

        val isEmpty: Boolean = true,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object ToAuthorization : Navigation
        }

        data class Toast(val text: String) : Effect
    }
}
