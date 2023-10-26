package app.mybad.notifier.ui.screens.authorization.passwords

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class PasswordRecoveryScreenContract {
    sealed interface Event : ViewEvent {
        data class Recovery(val email: String) : Event
        data class UpdateEmail(val newEmail: String) : Event
        object ActionBack : Event
    }

    data class State(
        val email: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isRecoveryButtonEnabled: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToAuthorization : Navigation
            data class ToCodeVerification(val email: String) : Navigation
            object Back : Navigation
        }
    }
}
