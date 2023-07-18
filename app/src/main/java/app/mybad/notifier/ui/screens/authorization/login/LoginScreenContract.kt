package app.mybad.notifier.ui.screens.authorization.login

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class LoginScreenContract {

    sealed class Event : ViewEvent {
        data class LoginWithEmail(val email: String, val password: String) : Event()
        object ActionBack : Event()
        object ForgotPassword : Event()
        object LoginWithGoogle : Event()
        data class UpdateLogin(val newLogin: String) : Event()
        data class UpdatePassword(val newPassword: String) : Event()
    }

    data class State(
        val email: String,
        val password: String,
        val isLoggingByEmail: Boolean,
        val isLoggingByGoogle: Boolean,
        val isError: Boolean,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object LoginSuccessful : Effect()

        sealed class Navigation : Effect() {
            object ToForgotPassword : Navigation()
            object ToMain : Navigation()
            object Back : Navigation()
        }
    }
}