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
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.screens.reuse.OutlinedPasswordTextField
import app.mybad.notifier.ui.screens.reuse.Progress
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedTextField
import app.mybad.notifier.ui.screens.reuse.SignInWithGoogle
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun StartMainRegistrationScreen(
    state: RegistrationScreenContract.State,
    events: Flow<RegistrationScreenContract.Effect>? = null,
    onEventSent: (event: RegistrationScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: RegistrationScreenContract.Effect.Navigation) -> Unit
) {

    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                RegistrationScreenContract.Effect.Navigation.Back -> {
                    onNavigationRequested(RegistrationScreenContract.Effect.Navigation.Back)
                }

                RegistrationScreenContract.Effect.Navigation.ToMain -> {
                    onNavigationRequested(RegistrationScreenContract.Effect.Navigation.ToMain)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.authorization_screen_registration,
                onBackPressed = { onEventSent(RegistrationScreenContract.Event.ActionBack) }
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
                RegistrationScreenBaseForSignIn(state, onEventSent)
                Spacer(modifier = Modifier.height(16.dp))
                ReUseFilledButton(
                    modifier = Modifier.fillMaxWidth(),
                    textId = R.string.registration_create_account,
                    isEnabled = state.isRegistrationEnabled,
                    onClick = {
                        onEventSent(
                            RegistrationScreenContract.Event.CreateAccount(
                                email = state.email,
                                password = state.password,
                                confirmationPassword = state.confirmationPassword
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ClickableText(modifier = Modifier.fillMaxWidth(), text = getAgreementText()) {
                    onEventSent(RegistrationScreenContract.Event.ShowUserAgreement)
                }
                Spacer(modifier = Modifier.height(24.dp))
                SignInWithGoogle(
                    signInTextResource = R.string.continue_with,
                    onClick = { onEventSent(RegistrationScreenContract.Event.SignInWithGoogle) }
                )
            }
            if (state.isLoading) {
                Progress()
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
    state: RegistrationScreenContract.State,
    onEventSent: (event: RegistrationScreenContract.Event) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isEmailFormatError = state.error is RegistrationScreenContract.RegistrationError.WrongEmailFormat
        val isPasswordMismatch = state.error is RegistrationScreenContract.RegistrationError.PasswordsMismatch
        ReUseOutlinedTextField(
            value = state.email,
            label = stringResource(id = R.string.login_email),
            onValueChanged = { onEventSent(RegistrationScreenContract.Event.UpdateEmail(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = Next
            ),
            isError = isEmailFormatError,
            errorTextId = if (isEmailFormatError) R.string.incorrect_email else null
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedPasswordTextField(
            value = state.password,
            label = stringResource(id = R.string.login_password),
            onValueChanged = { onEventSent(RegistrationScreenContract.Event.UpdatePassword(it)) },
            isError = state.error is RegistrationScreenContract.RegistrationError.WrongPassword,
            errorTextId = R.string.password_format
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedPasswordTextField(
            value = state.confirmationPassword,
            label = stringResource(id = R.string.login_password_confirm),
            onValueChanged = { onEventSent(RegistrationScreenContract.Event.UpdateConfirmationPassword(it)) },
            isError = isPasswordMismatch,
            errorTextId = if (isPasswordMismatch) R.string.password_mismatch else null
        )
    }
}


