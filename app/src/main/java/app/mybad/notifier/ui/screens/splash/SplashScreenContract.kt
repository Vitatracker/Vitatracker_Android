package app.mybad.notifier.ui.screens.splash

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SplashScreenContract {
    sealed interface Event : ViewEvent {
        object OnAuthorization : Event
    }

    data class State(
        val startButtonVisible: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToMain : Navigation
            object ToAuthorization : Navigation
        }
    }
}
