package app.mybad.notifier.ui.screens.authorization.start

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class AuthorizationContract {
    sealed interface Event : ViewEvent {
        object Registration : Event
        object SignIn : Event
        object SignInWithGoogle : Event
    }

    object State : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToAuthorization : Navigation
            object ToRegistration : Navigation
        }
    }
}
