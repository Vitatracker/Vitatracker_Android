package app.mybad.notifier.ui.screens.settings.profile

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsProfileContract {
    sealed interface Event : ViewEvent {
        data object ActionBack : Event
        data object ChangePassword : Event
        data object SignOut : Event
        data object Cancel : Event
        data object DeleteAccount : Event
        data object DeleteConfirmation : Event
        data class OnUserNameChanged(val name: String = "") : Event
    }

    data class State(
        val name: String = "",
        val email: String = "",
        val isLoading: Boolean = false,
        val confirmDelete: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object ToChangePassword : Navigation
            data object ToAuthorization : Navigation
            data object Back : Navigation
        }
    }
}
