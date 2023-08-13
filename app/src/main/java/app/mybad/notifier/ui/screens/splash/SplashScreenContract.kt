package app.mybad.notifier.ui.screens.splash

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SplashScreenContract {
    sealed class Event : ViewEvent {
        object OnStartClicked : Event()
        object OnAuthorization : Event()
    }

    data class State(
        val startButtonVisible: Boolean = false,
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object ShowButton : Effect()
        sealed class Navigation : Effect() {
            object ToMain : Navigation()
            object ToAuthorization : Navigation()
        }
    }
}
