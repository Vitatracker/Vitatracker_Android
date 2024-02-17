package app.mybad.notifier.ui.screens.authorization.start

import android.content.Intent
import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState
import net.openid.appauth.TokenRequest

class AuthorizationContract {
    sealed interface Event : ViewEvent {
        data object Registration : Event
        data object SignIn : Event
        data object SignInWithGoogle : Event
        data object OpenGoogleLoginPage : Event
        data class TokenExchange(val tokenRequest: TokenRequest) : Event
    }

    object State : ViewState

    sealed interface Effect : ViewSideEffect {
        data class OpenAuthPage(val intent: Intent) : Effect
        sealed interface Navigation : Effect {
            data object ToAuthorization : Navigation
            data object ToRegistration : Navigation
            data object ToOpenGoogleLoginPage : Navigation
        }
    }
}
