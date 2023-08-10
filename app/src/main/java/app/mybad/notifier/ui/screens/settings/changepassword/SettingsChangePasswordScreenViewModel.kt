package app.mybad.notifier.ui.screens.settings.changepassword

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.user.LoadUserSettingsUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsChangePasswordScreenViewModel @Inject constructor(
    private val userSettingsUseCase: LoadUserSettingsUseCase
) :
    BaseViewModel<SettingsChangePasswordScreenContract.Event, SettingsChangePasswordScreenContract.State, SettingsChangePasswordScreenContract.Effect>() {

    init {
        setModelFromSaved()
    }

    private fun setModelFromSaved() {
        viewModelScope.launch(Dispatchers.IO) {
            val model = userSettingsUseCase.getUserPersonal()
            launch(Dispatchers.Main) {
                setState { copy(userAvatarUrl = model.avatar ?: "") }
            }
        }
    }


    override fun setInitialState(): SettingsChangePasswordScreenContract.State {
        return SettingsChangePasswordScreenContract.State(
            isLoading = false,
            error = SettingsChangePasswordScreenContract.StateErrors.Empty,
            "",
            "",
            "",
            ""
        )
    }

    override fun handleEvents(event: SettingsChangePasswordScreenContract.Event) {
        when (event) {
            SettingsChangePasswordScreenContract.Event.ActionBack -> setEffect { SettingsChangePasswordScreenContract.Effect.Navigation.Back }
            SettingsChangePasswordScreenContract.Event.Cancel -> setEffect { SettingsChangePasswordScreenContract.Effect.Navigation.Back }
            is SettingsChangePasswordScreenContract.Event.OnCurrentPasswordChanged -> setState { copy(currentPassword = event.newValue) }
            is SettingsChangePasswordScreenContract.Event.OnNewPasswordChanged -> setState { copy(newPassword = event.newValue) }
            is SettingsChangePasswordScreenContract.Event.OnNewPasswordConfirmChanged -> setState { copy(newPasswordRepeat = event.newValue) }
            SettingsChangePasswordScreenContract.Event.Save -> {
                tryChangePassword()
            }
        }
    }

    private fun tryChangePassword() {
        val newPass = viewState.value.newPassword
        val newPassConfirmed = viewState.value.newPasswordRepeat
        val oldPass = viewState.value.currentPassword

        if (oldPass.isEmpty()) {
            setState { copy(error = SettingsChangePasswordScreenContract.StateErrors.OldPasswordIsInvalid) }
            return
        }
        if (newPass != newPassConfirmed) {
            setState { copy(error = SettingsChangePasswordScreenContract.StateErrors.PasswordsMismatch) }
            return
        }
        if (!newPass.isValidPassword()) {
            setState { copy(error = SettingsChangePasswordScreenContract.StateErrors.NewPasswordIsInvalid) }
            return
        }

        setState { copy(isLoading = true, error = SettingsChangePasswordScreenContract.StateErrors.Empty) }
        viewModelScope.launch(Dispatchers.IO) {
            // TODO do password change
            delay(2000L)
            launch(Dispatchers.Main) {
                setState { copy(isLoading = false) }
                setEffect { SettingsChangePasswordScreenContract.Effect.Navigation.Back }
            }
        }
    }
}