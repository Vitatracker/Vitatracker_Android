package app.mybad.notifier.ui.screens.settings.changepassword

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsChangePasswordContract {

    sealed class Event : ViewEvent {
        data object ActionBack : Event()
        data object Save : Event()
        data object Cancel : Event()
        data class OnCurrentPasswordChanged(val newValue: String) : Event()
        data class OnNewPasswordChanged(val newValue: String) : Event()
        data class OnNewPasswordConfirmChanged(val newValue: String) : Event()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: StateErrors = StateErrors.Empty,
        val currentPassword: String = "",
        val newPassword: String = "",
        val newPasswordRepeat: String = ""
    ) : ViewState {
        val oldPassIsInvalid: Boolean = error is StateErrors.OldPasswordIsInvalid
        val newPasswordFieldInvalid: Boolean = error is StateErrors.NewPasswordIsInvalid
        val newPasswordFieldError: Boolean = newPasswordFieldInvalid || error is StateErrors.PasswordsMismatch
        val newPasswordRepeatFieldError: Boolean = error is StateErrors.PasswordsMismatch
    }

    sealed class Effect : ViewSideEffect {

        sealed class Navigation : Effect() {
            data object Back : Navigation()
        }
    }

    sealed class StateErrors {
        data object Empty : StateErrors()
        data object PasswordsMismatch : StateErrors()
        data object OldPasswordIsInvalid : StateErrors()
        data object NewPasswordIsInvalid : StateErrors()
    }
}
