package app.mybad.notifier.ui.screens.authorization.registration

import android.content.Intent
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import app.mybad.notifier.ui.screens.authorization.login.LoginContract
import net.openid.appauth.TokenRequest

class RegistrationContract {
    sealed interface Event : ViewEvent {
        data class CreateAccount(
            val email: String,
            val password: String,
            val confirmationPassword: String
        ) : Event

        data object OnBack : Event
        data object SignInWithGoogle : Event
        data object OpenGoogleLoginPage : Event
        data class UpdateEmail(val newEmail: String) : Event
        data class UpdatePassword(val newPassword: String) : Event
        data class UpdateConfirmationPassword(val newConfirmationPassword: String) : Event
        data object ShowUserAgreement : Event
        data class TokenExchange(val tokenRequest: TokenRequest) : Event
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val confirmationPassword: String = "",
        val isLoading: Boolean = false,
        val isErrorEmail: Boolean = false,
        val isErrorPassword: Boolean = false,
        val error: RegistrationError? = null,
        val isRegistrationButtonEnabled: Boolean = false,
    ) : ViewState

    sealed interface RegistrationError {
        data object PasswordsMismatch : RegistrationError
        data object WrongEmailFormat : RegistrationError
        data object UserEmailExists : RegistrationError
        data object WrongPassword : RegistrationError
        data class Error(val message: String) : RegistrationError
    }

    sealed interface Effect : ViewSideEffect {
        data class OpenAuthPage(val intent: Intent) : Effect

        sealed interface Navigation : Effect {
            data object ToMain : Navigation
            data object Back : Navigation
        }
    }
}
