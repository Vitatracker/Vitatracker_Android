package app.mybad.notifier.ui.screens.settings.profile

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsProfileContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
        object ChangePassword : Event
        object SignOut : Event
        object DeleteAccount : Event
        data class OnUserNameChanged(val name: String = "") : Event
    }

    data class State(
        val name: String = "",
        val email: String = "",
        val isLoading: Boolean = false
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToChangePassword : Navigation
            object ToAuthorization : Navigation
            object Back : Navigation
        }
    }
}
