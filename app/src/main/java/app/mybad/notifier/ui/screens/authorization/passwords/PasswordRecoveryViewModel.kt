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
) : BaseViewModel<PasswordRecoveryScreenContract.Event,
        PasswordRecoveryScreenContract.State, PasswordRecoveryScreenContract.Effect>() {

    override fun setInitialState() = PasswordRecoveryScreenContract.State()

    override fun handleEvents(event: PasswordRecoveryScreenContract.Event) {
        when (event) {
            PasswordRecoveryScreenContract.Event.ActionBack -> {
                setEffect { PasswordRecoveryScreenContract.Effect.Navigation.Back }
            }

            is PasswordRecoveryScreenContract.Event.Recovery -> {
                submitRecoveryRequest(event.email)
            }

            is PasswordRecoveryScreenContract.Event.UpdateEmail -> setState {
                copy(
                    email = event.newEmail,
                    isRecoveryButtonEnabled = event.newEmail.isValidEmail()
                )
            }
        }
    }

    private fun submitRecoveryRequest(email: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            recoveryPasswordUseCase(email).onSuccess {
                setState { copy(isError = false, isLoading = false) }
                setEffect { PasswordRecoveryScreenContract.Effect.Navigation.ToCodeVerification(email) }
            }.onFailure {
                setState { copy(isError = true, isLoading = false) }
            }
        }
    }
}
