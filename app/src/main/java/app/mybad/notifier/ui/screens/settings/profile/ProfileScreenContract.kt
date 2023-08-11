package app.mybad.notifier.ui.screens.settings.profile

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class ProfileScreenContract {
    sealed class Event : ViewEvent {
        object ActionBack : Event()
        object ChangePassword : Event()
        object SignOut : Event()
        object DeleteAccount : Event()
        object EditAvatar : Event()
        data class OnUserNameChanged(val name: String) : Event()
    }

    data class State(
        val userAvatar: String,
        val name: String,
        val email: String,
        val isLoading: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object ShowDialog : Effect()
        sealed class Navigation : Effect() {
            object ToChangePassword : Navigation()
            object ToAuthorization : Navigation()
            object Back : Navigation()
        }
    }
}