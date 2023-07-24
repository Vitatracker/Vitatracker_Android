package app.mybad.notifier.ui.screens.splash

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SplashScreenContract {
    sealed class Event : ViewEvent {
        object OnStartClicked : Event()
    }

    data class State(
        val startButtonVisible: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object ToMain : Navigation()
            object ToAuthorization : Navigation()
        }
    }
}