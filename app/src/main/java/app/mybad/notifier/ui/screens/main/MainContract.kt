package app.mybad.notifier.ui.screens.main

import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import app.mybad.utils.currentDateTimeSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

class MainContract {
    sealed interface Event : ViewEvent {
        data class ChangeDate(val date: LocalDateTime) : Event
        data class SetUsageFactTime(val usageKey: String) : Event
    }

    data class State(
        val patternsAndUsages: Map<String, UsageDisplayDomainModel> = emptyMap(),

        private val updateDate: LocalDateTime = currentDateTimeSystem(),

        val isEmpty: Boolean = true,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToAuthorization : Navigation
        }

        data class Toast(val text: String) : Effect
    }
}
