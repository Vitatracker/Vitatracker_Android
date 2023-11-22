package app.mybad.notifier.ui.screens.settings.notifications_request

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class NotificationRequestContract {
    sealed interface Event : ViewEvent {
        data object OnSettings : Event
        data object OnNext : Event
        data object OnReject : Event
    }

    data class State(
        val confirmation: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object ToSettings : Navigation
            data object ToNext : Navigation
        }
    }
}
