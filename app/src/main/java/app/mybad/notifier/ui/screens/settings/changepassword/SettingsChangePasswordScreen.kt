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
import app.mybad.notifier.ui.screens.reuse.OutlinedPasswordTextField
import app.mybad.notifier.ui.screens.reuse.Progress
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedButton
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsPasswordEdit(
    state: SettingsChangePasswordScreenContract.State,
    events: Flow<SettingsChangePasswordScreenContract.Effect>? = null,
    onEventSent: (event: SettingsChangePasswordScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: SettingsChangePasswordScreenContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                SettingsChangePasswordScreenContract.Effect.Navigation.Back -> {
                    onNavigationRequested(SettingsChangePasswordScreenContract.Effect.Navigation.Back)
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.settings_change_password,
                onBackPressed = { onEventSent(SettingsChangePasswordScreenContract.Event.ActionBack) }
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
            UserImage(url = state.userAvatarUrl, showEdit = false) { }
            Spacer(Modifier.height(32.dp))
            OutlinedPasswordTextField(
                value = state.currentPassword,
                label = stringResource(id = R.string.settings_current_password),
                onValueChanged = {
                    onEventSent(SettingsChangePasswordScreenContract.Event.OnCurrentPasswordChanged(it))
                },
                isError = state.oldPassIsInvalid,
                errorTextId = if (state.oldPassIsInvalid) R.string.password_format else null
            )
            Spacer(Modifier.height(8.dp))
            OutlinedPasswordTextField(
                value = state.newPassword,
                label = stringResource(id = R.string.settings_new_password),
                onValueChanged = {
                    onEventSent(SettingsChangePasswordScreenContract.Event.OnNewPasswordChanged(it))
                },
                isError = state.newPasswordFieldError,
                errorTextId = if (state.newPasswordFieldInvalid) R.string.password_format else null
            )

            Spacer(Modifier.height(8.dp))
            OutlinedPasswordTextField(
                value = state.newPasswordRepeat,
                label = stringResource(id = R.string.settings_repeat_new_password),
                onValueChanged = {
                    onEventSent(SettingsChangePasswordScreenContract.Event.OnNewPasswordConfirmChanged(it))
                },
                isError = state.newPasswordRepeatFieldError,
                errorTextId = if (state.newPasswordRepeatFieldError) R.string.password_mismatch else null
            )
            Spacer(Modifier.height(24.dp))
            Buttons(onEventSent)
        }
    }
    if (state.isLoading) {
        Progress()
    }
}

@Composable
@Preview
private fun Buttons(
    onEventSent: (event: SettingsChangePasswordScreenContract.Event) -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ReUseOutlinedButton(modifier = Modifier.weight(1f), textId = R.string.settings_cancel) {
            onEventSent(SettingsChangePasswordScreenContract.Event.Cancel)
        }
        Spacer(modifier = Modifier.width(16.dp))
        ReUseFilledButton(
            modifier = Modifier.weight(1f),
            textId = R.string.settings_save
        ) {
            onEventSent(SettingsChangePasswordScreenContract.Event.Save)
        }
    }
}
