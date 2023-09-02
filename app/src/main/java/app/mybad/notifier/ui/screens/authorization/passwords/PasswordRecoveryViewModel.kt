package app.mybad.notifier.ui.screens.authorization.passwords

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.authorization.RecoveryPasswordUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val recoveryPasswordUseCase: RecoveryPasswordUseCase,
) : BaseViewModel<PasswordRecoveryContract.Event,
        PasswordRecoveryContract.State, PasswordRecoveryContract.Effect>() {

    override fun setInitialState() = PasswordRecoveryContract.State()

    override fun handleEvents(event: PasswordRecoveryContract.Event) {
        when (event) {
            PasswordRecoveryContract.Event.ActionBack -> {
                setEffect { PasswordRecoveryContract.Effect.Navigation.Back }
            }

            is PasswordRecoveryContract.Event.Recovery -> {
                submitRecoveryRequest(event.email)
            }

            is PasswordRecoveryContract.Event.UpdateEmail -> setState {
                copy(
                    email = event.newEmail,
                    isRecoveryButtonEnabled = email.isValidEmail()
                )
            }
        }
    }

    private fun submitRecoveryRequest(email: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            recoveryPasswordUseCase(email).onSuccess {
                setState { copy(isError = false, isLoading = false) }
                setEffect { PasswordRecoveryContract.Effect.MessageSent(it.message) }
            }.onFailure {
                setState { copy(isError = true, isLoading = false) }
            }
        }
    }
}
