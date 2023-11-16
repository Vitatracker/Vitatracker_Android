package app.mybad.notifier.ui.screens.authorization.passwords

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseOutlinedTextField
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun StartPasswordRecoveryScreen(
    state: PasswordRecoveryScreenContract.State,
    effectFlow: Flow<PasswordRecoveryScreenContract.Effect>,
    sendEvent: (event: PasswordRecoveryScreenContract.Event) -> Unit = {},
    navigation: (navigationEffect: PasswordRecoveryScreenContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow.collect { effect ->
            navigation(effect as PasswordRecoveryScreenContract.Effect.Navigation)
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.password_recovery,
                onBackPressed = { sendEvent(PasswordRecoveryScreenContract.Event.ActionBack) }
            )
        }) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            PasswordRecoveryScreen(state = state, sendEvent = sendEvent)
        }
        if (state.isLoading) {
            ReUseProgressDialog()
        }
    }
}

@Composable
private fun PasswordRecoveryScreen(
    state: PasswordRecoveryScreenContract.State,
    sendEvent: (event: PasswordRecoveryScreenContract.Event) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.password_recovery_enter_mail),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        ReUseOutlinedTextField(
            value = state.email,
            label = stringResource(id = R.string.login_email),
            onValueChanged = {
                sendEvent(PasswordRecoveryScreenContract.Event.UpdateEmail(it))
            },
            isError = state.isError,
            errorTextId = null
        )
        Spacer(modifier = Modifier.height(32.dp))
        ReUseFilledButton(
            textId = R.string.action_recover,
            enabled = state.isRecoveryButtonEnabled
        ) {
            sendEvent(PasswordRecoveryScreenContract.Event.Recovery(state.email))
        }
    }
}
