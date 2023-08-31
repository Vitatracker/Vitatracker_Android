package app.mybad.notifier.ui.screens.authorization.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseOutlinedTextField
import app.mybad.notifier.ui.common.ReUsePasswordOutlinedTextField
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.SignInWithGoogle
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun StartRegistrationScreen(
    state: RegistrationContract.State,
    effectFlow: Flow<RegistrationContract.Effect>? = null,
    sendEvent: (event: RegistrationContract.Event) -> Unit = {},
    navigation: (navigationEffect: RegistrationContract.Effect.Navigation) -> Unit
) {

    LaunchedEffect(key1 = true) {
        effectFlow?.collect {effect->
            when (effect) {
                is RegistrationContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.authorization_screen_registration,
                onBackPressed = { sendEvent(RegistrationContract.Event.ActionBack) }
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
                    .padding(16.dp)
            ) {
                RegistrationScreenBaseForSignIn(state, sendEvent)
                Spacer(modifier = Modifier.height(16.dp))
                ReUseFilledButton(
                    modifier = Modifier.fillMaxWidth(),
                    textId = R.string.registration_create_account,
                    isEnabled = state.isRegistrationButtonEnabled,
                    onClick = {
                        sendEvent(
                            RegistrationContract.Event.CreateAccount(
                                email = state.email,
                                password = state.password,
                                confirmationPassword = state.confirmationPassword
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ClickableText(modifier = Modifier.fillMaxWidth(), text = getAgreementText()) {
                    sendEvent(RegistrationContract.Event.ShowUserAgreement)
                }
                Spacer(modifier = Modifier.height(24.dp))
                SignInWithGoogle(
                    signInTextResource = R.string.continue_with,
                    onClick = { sendEvent(RegistrationContract.Event.SignInWithGoogle) }
                )
            }
            if (state.isLoading) {
                ReUseProgressDialog()
            }
        }
    )
}

@Composable
private fun getAgreementText(): AnnotatedString {
    //Find where searchQuery appears in courseName
    val start = stringResource(id = R.string.login_agree_policy_text) + " "
    return buildAnnotatedString {
        append(start)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(stringResource(id = R.string.login_text_privacy_policy))
        }
    }
}

@Composable
private fun RegistrationScreenBaseForSignIn(
    state: RegistrationContract.State,
    sendEvent: (event: RegistrationContract.Event) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isEmailFormatError = state.error is RegistrationContract.RegistrationError.WrongEmailFormat
        val isPasswordMismatch = state.error is RegistrationContract.RegistrationError.PasswordsMismatch

        ReUseOutlinedTextField(
            value = state.email,
            label = stringResource(id = R.string.login_email),
            onValueChanged = { sendEvent(RegistrationContract.Event.UpdateEmail(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = isEmailFormatError,
            errorTextId = if (isEmailFormatError) R.string.incorrect_email else null
        )
        Spacer(modifier = Modifier.height(4.dp))

        ReUsePasswordOutlinedTextField(
            value = state.password,
            label = stringResource(id = R.string.login_password),
            onValueChanged = { sendEvent(RegistrationContract.Event.UpdatePassword(it)) },
            isError = state.error is RegistrationContract.RegistrationError.WrongPassword,
            errorTextId = R.string.password_format
        )
        Spacer(modifier = Modifier.height(4.dp))

        ReUsePasswordOutlinedTextField(
            value = state.confirmationPassword,
            label = stringResource(id = R.string.login_password_confirm),
            onValueChanged = { sendEvent(RegistrationContract.Event.UpdateConfirmationPassword(it)) },
            isError = isPasswordMismatch,
            errorTextId = if (isPasswordMismatch) R.string.password_mismatch else null
        )
    }
}