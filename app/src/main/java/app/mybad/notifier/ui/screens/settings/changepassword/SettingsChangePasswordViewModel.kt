package app.mybad.notifier.ui.screens.settings.changepassword

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.user.ChangePasswordUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
) : BaseViewModel<
    SettingsChangePasswordContract.Event,
    SettingsChangePasswordContract.State,
    SettingsChangePasswordContract.Effect>() {

    override fun setInitialState() = SettingsChangePasswordContract.State()

    override fun handleEvents(event: SettingsChangePasswordContract.Event) {
        when (event) {
            SettingsChangePasswordContract.Event.ActionBack -> {
                setEffect { SettingsChangePasswordContract.Effect.Navigation.Back }
            }

            SettingsChangePasswordContract.Event.Cancel -> {
                setEffect { SettingsChangePasswordContract.Effect.Navigation.Back }
            }

            is SettingsChangePasswordContract.Event.OnCurrentPasswordChanged -> setState {
                copy(
                    currentPassword = event.newValue
                )
            }

            is SettingsChangePasswordContract.Event.OnNewPasswordChanged -> setState {
                copy(
                    newPassword = event.newValue
                )
            }

            is SettingsChangePasswordContract.Event.OnNewPasswordConfirmChanged -> setState {
                copy(
                    newPasswordRepeat = event.newValue
                )
            }

            SettingsChangePasswordContract.Event.Save -> {
                tryChangePassword()
            }
        }
    }

    private fun tryChangePassword() {
        val newPass = viewState.value.newPassword
        val newPassConfirmed = viewState.value.newPasswordRepeat
        val oldPass = viewState.value.currentPassword

        if (!oldPass.isValidPassword()) {
            setState { copy(error = SettingsChangePasswordContract.StateErrors.OldPasswordIsInvalid) }
            return
        }
        if (newPass != newPassConfirmed) {
            setState { copy(error = SettingsChangePasswordContract.StateErrors.PasswordsMismatch) }
            return
        }
        if (!newPass.isValidPassword()) {
            setState { copy(error = SettingsChangePasswordContract.StateErrors.NewPasswordIsInvalid) }
            return
        }

        setState {
            copy(
                isLoading = true,
                error = SettingsChangePasswordContract.StateErrors.Empty
            )
        }
        viewModelScope.launch {
            changePasswordUseCase(oldPass = oldPass, newPass = newPass).onSuccess {
                Log.w(
                    "VTTAG",
                    "SettingsChangePasswordViewModel::changePasswordUseCase onSuccess: oldPass = $oldPass, newPass = $newPass"
                )

                setState {
                    copy(
                        error = SettingsChangePasswordContract.StateErrors.Empty,
                        isLoading = false,
                    )
                }
                setEffect { SettingsChangePasswordContract.Effect.Navigation.Back }
            }.onFailure { throwable ->
                Log.w(
                    "VTTAG",
                    "SettingsChangePasswordViewModel::changePasswordUseCase onFailure: message =${throwable.message}"
                )
                setState {
                    copy(
                        error = SettingsChangePasswordContract.StateErrors.NewPasswordIsInvalid,
                        isLoading = false,
                    )
                }
            }
        }
    }
}
