package app.mybad.notifier.ui.screens.settings.changepassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUsePasswordOutlinedTextField
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsChangePasswordScreen(
    state: SettingsChangePasswordContract.State,
    effectFlow: Flow<SettingsChangePasswordContract.Effect>? = null,
    sendEvent: (event: SettingsChangePasswordContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsChangePasswordContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsChangePasswordContract.Effect.Navigation -> navigation(effect)
            }
        }
    }
    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.settings_change_password,
                onBackPressed = { sendEvent(SettingsChangePasswordContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChangePasswordTop(state, sendEvent)
            ReUseFilledButton(
                textId = R.string.action_confirm
            ) {
                sendEvent(SettingsChangePasswordContract.Event.Save)
            }
        }
    }
    if (state.isLoading) {
        ReUseProgressDialog()
    }
}

@Composable
private fun ChangePasswordTop(
    state: SettingsChangePasswordContract.State,
    sendEvent: (event: SettingsChangePasswordContract.Event) -> Unit = {},
) {
    Column {
        ReUsePasswordOutlinedTextField(
            value = state.currentPassword,
            label = stringResource(id = R.string.settings_current_password),
            onValueChanged = {
                sendEvent(SettingsChangePasswordContract.Event.OnCurrentPasswordChanged(it))
            },
            isError = state.oldPassIsInvalid,
            errorTextId = if (state.oldPassIsInvalid) R.string.password_format else null
        )
        ReUsePasswordOutlinedTextField(
            value = state.newPassword,
            label = stringResource(id = R.string.settings_new_password),
            onValueChanged = {
                sendEvent(SettingsChangePasswordContract.Event.OnNewPasswordChanged(it))
            },
            isError = state.newPasswordFieldError,
            errorTextId = if (state.newPasswordFieldInvalid) R.string.password_format else null
        )

        ReUsePasswordOutlinedTextField(
            value = state.newPasswordRepeat,
            label = stringResource(id = R.string.settings_repeat_new_password),
            onValueChanged = {
                sendEvent(SettingsChangePasswordContract.Event.OnNewPasswordConfirmChanged(it))
            },
            isError = state.newPasswordRepeatFieldError,
            errorTextId = if (state.newPasswordRepeatFieldError) R.string.password_mismatch else null
        )
    }
}
