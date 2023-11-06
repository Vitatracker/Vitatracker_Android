package app.mybad.notifier.ui.screens.authorization.recoverycodeverification

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class RecoveryCodeVerificationScreenContract {

    sealed interface Event : ViewEvent {
        object SendVerificationCode : Event
        object ReSendCodeToEmail : Event
        data class UpdateCode(val code: String) : Event
        object ActionBack : Event
    }

    data class State(
        val isError: Boolean = false,
        val attemptsCountLeft: Int = 3,
        val verificationCode: String = "",
        val email: String = "",
        val reSendingCodeState: ReSendingCodeState = ReSendingCodeState.Initial,
        val sendingCodeState: SendingCodeState = SendingCodeState.NotActive
    ) : ViewState

    sealed class SendingCodeState {
        object Active : SendingCodeState()
        object Loading : SendingCodeState()
        object NotActive : SendingCodeState()
    }

    sealed class ReSendingCodeState {
        object Initial : ReSendingCodeState()
        object NotActive : ReSendingCodeState()
        data class AttemptingPeriod(val secondsToNextAttempt: Int) : ReSendingCodeState()
    }

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            class ToNewPassword(val token: String, val email: String) : Navigation
            object Back : Navigation
        }
    }
}