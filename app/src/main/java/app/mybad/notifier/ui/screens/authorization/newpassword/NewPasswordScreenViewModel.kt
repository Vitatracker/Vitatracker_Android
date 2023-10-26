package app.mybad.notifier.ui.screens.authorization.newpassword

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.authorization.SetNewPasswordUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordScreenViewModel @Inject constructor(
    private val setNewPasswordUseCase: SetNewPasswordUseCase
) :
    BaseViewModel<NewPasswordScreenContract.Event, NewPasswordScreenContract.State, NewPasswordScreenContract.Effect>() {

    private var passwordChangingToken: String = ""
    private var passwordChangingEmail: String = ""

    override fun setInitialState(): NewPasswordScreenContract.State = NewPasswordScreenContract.State()
    override fun handleEvents(event: NewPasswordScreenContract.Event) {
        when (event) {
            NewPasswordScreenContract.Event.ActionConfirm -> {
                confirmNewPassword()
            }

            is NewPasswordScreenContract.Event.UpdateNewPassword -> setState {
                copy(
                    newPassword = event.password,
                    isError = null,
                    isConfirmButtonEnabled = viewState.value.confirmPassword.isNotBlank() && event.password.isNotBlank()
                )
            }

            is NewPasswordScreenContract.Event.UpdateConfirmationPassword -> setState {
                copy(
                    confirmPassword = event.password,
                    isError = null,
                    isConfirmButtonEnabled = viewState.value.newPassword.isNotBlank() && event.password.isNotBlank()
                )
            }

            NewPasswordScreenContract.Event.ActionBack -> setEffect { NewPasswordScreenContract.Effect.Navigation.Back }
        }
    }

    private fun confirmNewPassword() {
        val currentState = viewState.value
        if (currentState.newPassword != currentState.confirmPassword) {
            setState {
                copy(isError = NewPasswordScreenContract.PasswordsError.PasswordsMismatch, isConfirmButtonEnabled = false)
            }
            return
        }
        if (!currentState.newPassword.isValidPassword()) {
            setState {
                copy(isError = NewPasswordScreenContract.PasswordsError.WrongPassword, isConfirmButtonEnabled = false)
            }
            return
        }
        viewModelScope.launch {
            setState { copy(isLoading = true, isError = null) }
            setNewPasswordUseCase.invoke(passwordChangingToken, viewState.value.newPassword, passwordChangingEmail)
                .onSuccess {
                    setEffect { NewPasswordScreenContract.Effect.Navigation.ToAuthorization }
                    setState { copy(isLoading = false, isError = null) }
                }
                .onFailure {
                    setState { copy(isLoading = false, isError = NewPasswordScreenContract.PasswordsError.WrongPassword) }
                }
        }
    }

    fun setNavigationParams(token: String, email: String) {
        passwordChangingToken = token
        passwordChangingEmail = email
    }
}