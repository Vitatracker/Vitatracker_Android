package app.mybad.notifier.ui.screens.authorization.passwords

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class PasswordRecoveryContract {
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
        data class MessageSent(val message: String) : Effect
        sealed interface Navigation : Effect {
            object ToAuthorization : Navigation
            object Back : Navigation
        }
    }
}
