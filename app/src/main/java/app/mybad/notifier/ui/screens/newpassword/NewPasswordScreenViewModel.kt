package app.mybad.notifier.ui.screens.newpassword

import app.mybad.notifier.ui.base.BaseViewModel

class NewPasswordScreenViewModel :
    BaseViewModel<NewPasswordScreenContract.Event, NewPasswordScreenContract.State, NewPasswordScreenContract.Effect>() {
    override fun setInitialState(): NewPasswordScreenContract.State = NewPasswordScreenContract.State()

    override fun handleEvents(event: NewPasswordScreenContract.Event) {
        when (event) {
            NewPasswordScreenContract.Event.ActionConfirm -> {

            }

            is NewPasswordScreenContract.Event.UpdateNewPassword -> setState {
                copy(
                    newPassword = event.password,
                    isError = false,
                    isConfirmButtonEnabled = viewState.value.confirmPassword.isNotBlank() && event.password.isNotBlank()
                )
            }

            is NewPasswordScreenContract.Event.UpdateConfirmationPassword -> setState {
                copy(
                    confirmPassword = event.password,
                    isError = false,
                    isConfirmButtonEnabled = viewState.value.newPassword.isNotBlank() && event.password.isNotBlank()
                )
            }
        }
    }
}