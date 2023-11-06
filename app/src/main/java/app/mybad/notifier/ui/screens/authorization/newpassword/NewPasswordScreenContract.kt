package app.mybad.notifier.ui.screens.authorization.newpassword

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class NewPasswordScreenContract {
    sealed interface Event : ViewEvent {
        object ActionConfirm : Event
        object ActionBack : Event
        data class UpdateNewPassword(val password: String) : Event
        data class UpdateConfirmationPassword(val password: String) : Event
    }

    data class State(
        val newPassword: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val isError: PasswordsError? = null,
        val isConfirmButtonEnabled: Boolean = false,
    ) : ViewState

    sealed interface PasswordsError {
        object PasswordsMismatch : PasswordsError
        object WrongPassword : PasswordsError
    }
    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToAuthorization : Navigation
            object Back : Navigation
        }
    }
}