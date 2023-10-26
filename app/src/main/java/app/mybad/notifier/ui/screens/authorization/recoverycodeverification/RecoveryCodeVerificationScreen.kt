package app.mybad.notifier.ui.screens.authorization.recoverycodeverification

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun RecoveryCodeVerificationScreen(
    state: RecoveryCodeVerificationScreenContract.State,
    effectFlow: Flow<RecoveryCodeVerificationScreenContract.Effect>? = null,
    sendEvent: (event: RecoveryCodeVerificationScreenContract.Event) -> Unit = {},
    navigation: (navigationEffect: RecoveryCodeVerificationScreenContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            if (effect is RecoveryCodeVerificationScreenContract.Effect.Navigation) {
                navigation(effect)
            }
        }
    }
    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.password_recovery,
                onBackPressed = { sendEvent(RecoveryCodeVerificationScreenContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 24.dp, end = 24.dp)
        ) {
            ScreenContent(state, sendEvent)
        }
    }
}

@Composable
private fun ScreenContent(
    state: RecoveryCodeVerificationScreenContract.State,
    sendEvent: (event: RecoveryCodeVerificationScreenContract.Event) -> Unit
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        fontSize = 14.sp,
        text = String.format(stringResource(id = R.string.verification_code_sent_to), state.email),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(40.dp))
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OtpTextField(otpText = state.verificationCode, isError = state.isError) {
            sendEvent(RecoveryCodeVerificationScreenContract.Event.UpdateCode(it))
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
    val (resendCodeText, color) = when (state.reSendingCodeState) {
        is RecoveryCodeVerificationScreenContract.ReSendingCodeState.AttemptingPeriod -> {
            Pair(
                state.reSendingCodeState.secondsToNextAttempt.toString(),
                MaterialTheme.colorScheme.outline
            )
        }

        RecoveryCodeVerificationScreenContract.ReSendingCodeState.Initial -> {
            Pair(
                stringResource(id = R.string.resend_verification_code),
                MaterialTheme.colorScheme.primary
            )
        }

        RecoveryCodeVerificationScreenContract.ReSendingCodeState.NotActive -> {
            Pair(
                stringResource(id = R.string.resend_verification_code_loading),
                MaterialTheme.colorScheme.outline
            )
        }
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = state.reSendingCodeState is RecoveryCodeVerificationScreenContract.ReSendingCodeState.Initial) {
                sendEvent(RecoveryCodeVerificationScreenContract.Event.ReSendCodeToEmail)
            },
        text = resendCodeText,
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = pluralStringResource(
            id = R.plurals.verification_code_attempts_left,
            count = state.attemptsCountLeft,
            state.attemptsCountLeft
        ),
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))
    if (state.sendingCodeState is RecoveryCodeVerificationScreenContract.SendingCodeState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxHeight()
            )
        }

    } else {
        ReUseFilledButton(
            modifier = Modifier.fillMaxWidth(),
            isEnabled = state.sendingCodeState is RecoveryCodeVerificationScreenContract.SendingCodeState.Active,
            textId = R.string.text_continue
        ) {
            sendEvent(RecoveryCodeVerificationScreenContract.Event.SendVerificationCode)
        }
    }
}

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    isError: Boolean,
    onOtpTextChange: (String) -> Unit
) {

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText,
                        isError = isError
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String,
    isError: Boolean,
) {
    val isFocused = text.length == index
    val char = when {
        index >= text.length -> ""
        else -> text[index].toString()
    }
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = when {
                    isError -> MaterialTheme.colorScheme.error
                    isFocused -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                },
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .wrapContentHeight(),
            text = char,
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
fun RecoveryCodeVerificationScreenPreview() {
    MyBADTheme {
        val viewModel: RecoveryCodeVerificationViewModel = hiltViewModel()
        viewModel.setEmail("testing@gmail.com")
        RecoveryCodeVerificationScreen(
            state = viewModel.viewState.value,
            effectFlow = null,
            sendEvent = viewModel::setEvent,
            navigation = {}
        )
    }
}

