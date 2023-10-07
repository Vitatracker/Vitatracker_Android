package app.mybad.notifier.ui.screens.newpassword

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class NewPasswordScreenContract {
    sealed interface Event : ViewEvent {
        object ActionConfirm : Event
        data class UpdateNewPassword(val password: String) : Event
        data class UpdateConfirmationPassword(val password: String) : Event
    }

    data class State(
        val newPassword: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isConfirmButtonEnabled: Boolean = false,
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToAuthorization : Navigation
        }
    }
}
