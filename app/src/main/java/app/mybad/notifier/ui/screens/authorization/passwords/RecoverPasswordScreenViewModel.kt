package app.mybad.notifier.ui.screens.authorization.passwords

import androidx.lifecycle.viewModelScope
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordScreenViewModel @Inject constructor() :
    BaseViewModel<RecoverPasswordScreenContract.Event, RecoverPasswordScreenContract.State, RecoverPasswordScreenContract.Effect>() {

    override fun setInitialState(): RecoverPasswordScreenContract.State {
        return RecoverPasswordScreenContract.State(
            email = "",
            isLoading = false,
            isError = false,
            isRecoveringEnabled = false
        )
    }

    override fun handleEvents(event: RecoverPasswordScreenContract.Event) {
        when (event) {
            RecoverPasswordScreenContract.Event.ActionBack -> setEffect { RecoverPasswordScreenContract.Effect.Navigation.Back }
            is RecoverPasswordScreenContract.Event.Recover -> {
                sendRecoveringRequest(event.email)
            }

            is RecoverPasswordScreenContract.Event.UpdateEmail -> setState {
                copy(
                    email = event.newEmail,
                    isRecoveringEnabled = email.isValidEmail()
                )
            }
        }
    }

    private fun sendRecoveringRequest(email: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            delay(1000L)
            setState { copy(isLoading = false) }
            setEffect { RecoverPasswordScreenContract.Effect.MessageSent }
        }
    }
}