package app.mybad.notifier.ui.screens.authorization.passwords

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.screens.reuse.Progress
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedTextField
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainRecoveryPasswordScreenAuth(
    state: RecoverPasswordScreenContract.State,
    events: Flow<RecoverPasswordScreenContract.Effect>? = null,
    onEventSent: (event: RecoverPasswordScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: RecoverPasswordScreenContract.Effect.Navigation) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
        events?.collect { effect ->
            when (effect) {
                RecoverPasswordScreenContract.Effect.Navigation.Back -> {
                    onNavigationRequested(RecoverPasswordScreenContract.Effect.Navigation.Back)
                }

                RecoverPasswordScreenContract.Effect.Navigation.ToAuthorization -> {
                    onNavigationRequested(RecoverPasswordScreenContract.Effect.Navigation.ToAuthorization)
                }

                RecoverPasswordScreenContract.Effect.MessageSent -> showDialog = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.password_recovery,
                onBackPressed = { onEventSent(RecoverPasswordScreenContract.Event.ActionBack) }
            )
        }) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            MainRecoveryPasswordScreenAuth(state = state, onEventSent = onEventSent)
        }
        if (state.isLoading) {
            Progress()
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                onEventSent(RecoverPasswordScreenContract.Event.ActionBack)
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.secondary,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(id = R.string.password_recovery),
                        fontSize = 24.sp,
                        fontWeight = FontWeight(400)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = String.format(stringResource(id = R.string.password_recovery_dialog_body), state.email),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            showDialog = false
                            onEventSent(RecoverPasswordScreenContract.Event.ActionBack)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = stringResource(id = R.string.action_navigate), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MainRecoveryPasswordScreenAuth(
    state: RecoverPasswordScreenContract.State,
    onEventSent: (event: RecoverPasswordScreenContract.Event) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.fillMaxWidth(), text = stringResource(id = R.string.password_recovery_enter_mail), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))
        ReUseOutlinedTextField(
            value = state.email,
            label = stringResource(id = R.string.login_email),
            onValueChanged = { onEventSent(RecoverPasswordScreenContract.Event.UpdateEmail(it)) },
            isError = state.isError,
            errorTextId = null
        )
        Spacer(modifier = Modifier.height(32.dp))
        ReUseFilledButton(
            modifier = Modifier.fillMaxWidth(),
            textId = R.string.action_recover,
            isEnabled = state.isRecoveringEnabled
        ) {
            onEventSent(RecoverPasswordScreenContract.Event.Recover(state.email))
        }
    }
}
