package app.mybad.notifier.ui.screens.authorization.registration

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class RegistrationScreenContract {
    sealed class Event : ViewEvent {
        data class CreateAccount(val email: String, val password: String, val confirmationPassword: String) : Event()
        object ActionBack : Event()
        object SignInWithGoogle : Event()
        data class UpdateEmail(val newEmail: String) : Event()
        data class UpdatePassword(val newPassword: String) : Event()
        data class UpdateConfirmationPassword(val newConfirmationPassword: String) : Event()
        object ShowUserAgreement : Event()
    }

    data class State(
        val email: String,
        val password: String,
        val confirmationPassword: String,
        val isLoading: Boolean,
        val error: RegistrationError?,
        val isRegistrationEnabled: Boolean
    ) : ViewState

    sealed class RegistrationError {
        object PasswordsMismatch: RegistrationError()
        object WrongEmailFormat: RegistrationError()
        object WrongPassword: RegistrationError()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object ToMain : Navigation()
            object Back : Navigation()
        }
    }
}