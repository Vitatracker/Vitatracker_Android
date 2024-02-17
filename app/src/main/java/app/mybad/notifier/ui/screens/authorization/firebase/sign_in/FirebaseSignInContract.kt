package app.mybad.notifier.ui.screens.authorization.firebase.sign_in

import android.app.PendingIntent
import android.content.Intent
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import app.mybad.notifier.ui.screens.authorization.login.LoginContract
import net.openid.appauth.TokenRequest

class FirebaseSignInContract {

    sealed interface Event : ViewEvent {
        data object ActionBack : Event
        data object OpenGoogleLoginPage : Event
        data class SignInWithGoogle(val intent: Intent) : Event
        data class SignIn(val email: String, val password: String) : Event
    }

    data class State(
        val isSignInSuccessful: Boolean = false,
        val signInError: String? = null,

        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isErrorEmail: Boolean = false,
        val isErrorPassword: Boolean = false,
        val isLoginButtonEnabled: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        data class OpenAuthPage(val intent: PendingIntent) : Effect
        sealed interface Navigation : Effect {
            data object ToMain : Navigation
            data object Back : Navigation
        }
    }
}
