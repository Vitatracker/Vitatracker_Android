package app.mybad.notifier.ui.screens.authorization.login

import android.content.Intent
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import net.openid.appauth.TokenRequest

class LoginContract {

    sealed interface Event : ViewEvent {
        data class SignIn(val email: String, val password: String) : Event
        data object ActionBack : Event
        data object ForgotPassword : Event
        data object SignInWithGoogle : Event
        data object OpenGoogleLoginPage : Event
        data class UpdateLogin(val newLogin: String) : Event
        data class UpdatePassword(val newPassword: String) : Event
        data class TokenExchange(val tokenRequest: TokenRequest) : Event
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isErrorEmail: Boolean = false,
        val isErrorPassword: Boolean = false,
        val isLoginButtonEnabled: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        data class OpenAuthPage(val intent: Intent) : Effect

        sealed interface Navigation : Effect {
            data object ToForgotPassword : Navigation
            data object ToMain : Navigation
            data object Back : Navigation
        }
    }
}
