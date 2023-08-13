package app.mybad.notifier.ui.screens.authorization.login

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class LoginContract {

    sealed interface Event : ViewEvent {
        data class SignIn(val email: String, val password: String) : Event
        object ActionBack : Event
        object ForgotPassword : Event
        object SignInWithGoogle : Event
        data class UpdateLogin(val newLogin: String) : Event
        data class UpdatePassword(val newPassword: String) : Event
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
        sealed interface Navigation : Effect {
            object ToForgotPassword : Navigation
            object ToMain : Navigation
            object Back : Navigation
        }
    }
}
