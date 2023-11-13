package app.mybad.notifier.ui.screens.authorization.recoverycodeverification

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.authorization.RecoveryPasswordUseCase
import app.mybad.domain.usecases.authorization.SendVerificationCodeUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import app.mybad.notifier.ui.screens.authorization.recoverycodeverification.RecoveryCodeVerificationScreenContract as Contract

@HiltViewModel
class RecoveryCodeVerificationViewModel @Inject constructor(
    private val recoveryPasswordUseCase: RecoveryPasswordUseCase,
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase
) : BaseViewModel<Contract.Event,
        Contract.State,
        Contract.Effect>() {

    private val timer = object : CountDownTimer(60_000, 1_000) {
        override fun onTick(millisUntilFinished: Long) {
            viewModelScope.launch {
                val secondsLeft = millisUntilFinished.toInt() / 1000
                setState { copy(reSendingCodeState = Contract.ReSendingCodeState.AttemptingPeriod(secondsLeft)) }
            }
        }

        override fun onFinish() {
            viewModelScope.launch {
                setState { copy(reSendingCodeState = Contract.ReSendingCodeState.Initial) }
            }
        }
    }

    override fun setInitialState(): Contract.State = Contract.State()

    override fun handleEvents(event: Contract.Event) {
        when (event) {
            Contract.Event.ActionBack -> setEffect { Contract.Effect.Navigation.Back }
            Contract.Event.ReSendCodeToEmail -> resendCodeToEmail()
            Contract.Event.SendVerificationCode -> sendVerificationCode()
            is Contract.Event.UpdateCode -> {
                if (event.code.any { !it.isDigit() }) return
                val sendingCodeState = if (event.code.length == 6)
                    Contract.SendingCodeState.Active
                else
                    Contract.SendingCodeState.NotActive
                setState {
                    copy(
                        isError = false,
                        verificationCode = event.code,
                        sendingCodeState = sendingCodeState
                    )
                }
            }
        }
    }

    private fun sendVerificationCode() {
        val currentState = viewState.value
        if (currentState.attemptsCountLeft == 0 || currentState.verificationCode.length < 6) return
        viewModelScope.launch {
            setState {
                copy(
                    attemptsCountLeft = currentState.attemptsCountLeft - 1,
                    sendingCodeState = Contract.SendingCodeState.Loading
                )
            }
            sendVerificationCodeUseCase(currentState.verificationCode.toInt()).onSuccess {
                setState {
                    copy(
                        isError = false,
                        sendingCodeState = Contract.SendingCodeState.NotActive
                    )
                }
                setEffect { Contract.Effect.Navigation.ToNewPassword(it.token, currentState.email) }
            }.onFailure {
                Log.d("VTTAG", it.message.toString())
                setState {
                    copy(
                        isError = true,
                        sendingCodeState = Contract.SendingCodeState.NotActive
                    )
                }
            }
        }
    }

    private fun resendCodeToEmail() {
        viewModelScope.launch {
            setState {
                copy(reSendingCodeState = Contract.ReSendingCodeState.NotActive)
            }
            recoveryPasswordUseCase(viewState.value.email).onSuccess {
                setState { copy(attemptsCountLeft = 3) }
                timer.start()
            }.onFailure {
                setState { copy(reSendingCodeState = Contract.ReSendingCodeState.Initial) }
            }
        }
    }

    fun setEmail(email: String) {
        setState { copy(email = email) }
    }
}