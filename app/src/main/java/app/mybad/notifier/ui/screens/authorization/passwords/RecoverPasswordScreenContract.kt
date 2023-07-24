package app.mybad.notifier.ui.screens.authorization.passwords

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class RecoverPasswordScreenContract {
    sealed class Event : ViewEvent {
        data class Recover(val email: String) : Event()
        object ActionBack : Event()
        data class UpdateEmail(val newEmail: String) : Event()
    }

    data class State(
        val email: String,
        val isLoading: Boolean,
        val isError: Boolean,
        val isRecoveringEnabled: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object MessageSent : Effect()
        sealed class Navigation : Effect() {
            object ToAuthorization : Navigation()
            object Back : Navigation()
        }
    }
}