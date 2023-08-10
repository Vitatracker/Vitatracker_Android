package app.mybad.notifier.ui.screens.settings.changepassword

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsChangePasswordScreenContract {

    sealed class Event : ViewEvent {
        object ActionBack : Event()
        object Save : Event()
        object Cancel : Event()
        data class OnCurrentPasswordChanged(val newValue: String) : Event()
        data class OnNewPasswordChanged(val newValue: String) : Event()
        data class OnNewPasswordConfirmChanged(val newValue: String) : Event()
    }

    data class State(
        val isLoading: Boolean,
        val error: StateErrors,
        val userAvatarUrl: String,
        val currentPassword: String,
        val newPassword: String,
        val newPasswordRepeat: String
    ) : ViewState {
        val oldPassIsInvalid: Boolean = error is StateErrors.OldPasswordIsInvalid
        val newPasswordFieldInvalid: Boolean = error is StateErrors.NewPasswordIsInvalid
        val newPasswordFieldError: Boolean = newPasswordFieldInvalid || error is StateErrors.PasswordsMismatch
        val newPasswordRepeatFieldError: Boolean = error is StateErrors.PasswordsMismatch
    }

    sealed class Effect : ViewSideEffect {

        sealed class Navigation : Effect() {
            object Back : Navigation()
        }
    }

    sealed class StateErrors {
        object Empty : StateErrors()
        object PasswordsMismatch : StateErrors()
        object OldPasswordIsInvalid : StateErrors()
        object NewPasswordIsInvalid : StateErrors()
    }
}