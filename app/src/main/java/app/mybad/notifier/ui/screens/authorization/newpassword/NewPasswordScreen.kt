package app.mybad.notifier.ui.screens.authorization.newpassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseAnimatedVisibility
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUsePasswordOutlinedTextField
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun NewPasswordScreen(
    state: NewPasswordScreenContract.State,
    effectFlow: Flow<NewPasswordScreenContract.Effect>? = null,
    sendEvent: (event: NewPasswordScreenContract.Event) -> Unit = {},
    navigation: (navigationEffect: NewPasswordScreenContract.Effect.Navigation) -> Unit = {}
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is NewPasswordScreenContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.settings_new_password,
                onBackPressed = { sendEvent(NewPasswordScreenContract.Event.ActionBack) }
            )
        })
    { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val isPasswordsMismatch =
                    state.isError is NewPasswordScreenContract.PasswordsError.PasswordsMismatch
                val isWrongPassword =
                    state.isError is NewPasswordScreenContract.PasswordsError.WrongPassword
                ReUsePasswordOutlinedTextField(
                    value = state.newPassword,
                    errorTextId = R.string.password_format,
                    placeholder = R.string.settings_new_password,
                    isError = isPasswordsMismatch || isWrongPassword,
                    onValueChanged = {
                        sendEvent(NewPasswordScreenContract.Event.UpdateNewPassword(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ReUsePasswordOutlinedTextField(
                    value = state.confirmPassword,
                    placeholder = R.string.settings_repeat_new_password,
                    isError = isPasswordsMismatch,
                    errorTextId = if (isPasswordsMismatch) R.string.password_mismatch else null,
                    onValueChanged = {
                        sendEvent(NewPasswordScreenContract.Event.UpdateConfirmationPassword(it))
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                ReUseFilledButton(
                    textId = R.string.action_confirm
                ) {
                    sendEvent(NewPasswordScreenContract.Event.ActionConfirm)
                }
            }
        }
        ReUseAnimatedVisibility(state.isLoading) {
            ReUseProgressDialog()
        }
    }
}

@Preview
@Composable
fun PreviewNewPasswordScreen() {
    MyBADTheme {
        NewPasswordScreen(NewPasswordScreenContract.State())
    }
}
