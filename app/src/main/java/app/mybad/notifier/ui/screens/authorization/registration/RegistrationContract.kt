package app.mybad.notifier.ui.screens.authorization.registration

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class RegistrationContract {
    sealed interface Event : ViewEvent {
        data class CreateAccount(
            val email: String,
            val password: String,
            val confirmationPassword: String
        ) : Event

        object ActionBack : Event
        object SignInWithGoogle : Event
        data class UpdateEmail(val newEmail: String) : Event
        data class UpdatePassword(val newPassword: String) : Event
        data class UpdateConfirmationPassword(val newConfirmationPassword: String) : Event
        object ShowUserAgreement : Event
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val confirmationPassword: String = "",
        val isLoading: Boolean = false,
        val error: RegistrationError? = null,
        val isRegistrationButtonEnabled: Boolean = false,
    ) : ViewState

    sealed interface RegistrationError {
        object PasswordsMismatch : RegistrationError
        object WrongEmailFormat : RegistrationError
        object WrongPassword : RegistrationError
    }

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToMain : Navigation
            object Back : Navigation
        }
    }
}
