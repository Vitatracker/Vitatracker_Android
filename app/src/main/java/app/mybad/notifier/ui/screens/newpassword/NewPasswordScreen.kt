package app.mybad.notifier.ui.screens.newpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUsePasswordOutlinedTextField
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartNewPasswordScreen(
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
            CenterAlignedTopAppBar(
                title = {
                    TitleText(textStringRes = R.string.settings_new_password)
                }
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
                ReUsePasswordOutlinedTextField(
                    value = state.newPassword,
                    label = stringResource(id = R.string.settings_new_password),
                    isError = state.isError,
                    onValueChanged = {
                        sendEvent(NewPasswordScreenContract.Event.UpdateNewPassword(it))
                    }
                )
                ReUsePasswordOutlinedTextField(
                    value = state.confirmPassword,
                    label = stringResource(id = R.string.settings_repeat_new_password),
                    isError = state.isError,
                    onValueChanged = {
                        sendEvent(NewPasswordScreenContract.Event.UpdateConfirmationPassword(it))
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                ReUseFilledButton(
                    textId = R.string.text_continue
                ) {
                    sendEvent(NewPasswordScreenContract.Event.ActionConfirm)
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewNewPasswordScreen() {
    MyBADTheme {
        StartNewPasswordScreen(NewPasswordScreenContract.State())
    }
}
