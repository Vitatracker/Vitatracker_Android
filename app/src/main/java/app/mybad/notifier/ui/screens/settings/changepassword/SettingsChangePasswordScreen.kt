package app.mybad.notifier.ui.screens.settings.changepassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseOutlinedButton
import app.mybad.notifier.ui.common.ReUsePasswordOutlinedTextField
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.settings.common.UserImage
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
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserImage(url = state.userAvatarUrl, showEdit = false) {
                //TODO(" доделать")
            }
            Spacer(Modifier.height(32.dp))
            ReUsePasswordOutlinedTextField(
                value = state.currentPassword,
                label = stringResource(id = R.string.settings_current_password),
                onValueChanged = {
                    sendEvent(SettingsChangePasswordContract.Event.OnCurrentPasswordChanged(it))
                },
                isError = state.oldPassIsInvalid,
                errorTextId = if (state.oldPassIsInvalid) R.string.password_format else null
            )
            Spacer(Modifier.height(8.dp))
            ReUsePasswordOutlinedTextField(
                value = state.newPassword,
                label = stringResource(id = R.string.settings_new_password),
                onValueChanged = {
                    sendEvent(SettingsChangePasswordContract.Event.OnNewPasswordChanged(it))
                },
                isError = state.newPasswordFieldError,
                errorTextId = if (state.newPasswordFieldInvalid) R.string.password_format else null
            )

            Spacer(Modifier.height(8.dp))
            ReUsePasswordOutlinedTextField(
                value = state.newPasswordRepeat,
                label = stringResource(id = R.string.settings_repeat_new_password),
                onValueChanged = {
                    sendEvent(SettingsChangePasswordContract.Event.OnNewPasswordConfirmChanged(it))
                },
                isError = state.newPasswordRepeatFieldError,
                errorTextId = if (state.newPasswordRepeatFieldError) R.string.password_mismatch else null
            )
            Spacer(Modifier.height(24.dp))
            SaveAndCancelButtons(sendEvent)
        }
    }
    if (state.isLoading) {
        ReUseProgressDialog()
    }
}

@Composable
@Preview
private fun SaveAndCancelButtons(
    sendEvent: (event: SettingsChangePasswordContract.Event) -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ReUseOutlinedButton(
            modifier = Modifier.weight(1f),
            textId = R.string.settings_cancel
        ) {
            sendEvent(SettingsChangePasswordContract.Event.Cancel)
        }
        Spacer(modifier = Modifier.width(16.dp))
        ReUseFilledButton(
            modifier = Modifier.weight(1f),
            textId = R.string.settings_save
        ) {
            sendEvent(SettingsChangePasswordContract.Event.Save)
        }
    }
}
