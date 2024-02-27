package app.mybad.notifier.ui.screens.authorization.registration

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.AuthToken
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseAnimatedVisibility
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseOutlinedTextField
import app.mybad.notifier.ui.common.ReUsePasswordOutlinedTextField
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.SignInWithGoogle
import app.mybad.notifier.ui.common.showToast
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

@Composable
fun StartRegistrationScreen(
    state: RegistrationContract.State,
    effectFlow: Flow<RegistrationContract.Effect>? = null,
    sendEvent: (event: RegistrationContract.Event) -> Unit = {},
    navigation: (navigationEffect: RegistrationContract.Effect.Navigation) -> Unit = {},
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activity ->
        activity.data?.let { intent ->
            val exception = AuthorizationException.fromIntent(intent)
            if (exception == null && activity.resultCode == Activity.RESULT_OK) {
                Log.w("VTTAG", "AuthFragment|extractTokenCallback get")
                AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest()?.let {
                    sendEvent(RegistrationContract.Event.TokenExchange(it))
                } ?: context.showToast("Error: TokenExchange is null")
            } else {
                Log.w(
                    "VTTAG",
                    "AuthFragment|extractTokenCallback resultCode=${activity.resultCode}"
                )
                context.showToast("Error: Authorization - ${exception?.localizedMessage}")
            }
        }

        sendEvent(RegistrationContract.Event.SignInWithGoogle)
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is RegistrationContract.Effect.Navigation -> navigation(effect)
                is RegistrationContract.Effect.OpenAuthPage -> {
                    if (effect.intent.resolveActivity(context.packageManager) != null) {
                        launcher.launch(effect.intent)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.authorization_screen_registration,
                onBackPressed = { sendEvent(RegistrationContract.Event.OnBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            RegistrationScreenBaseForSignIn(state, sendEvent)
            Spacer(modifier = Modifier.height(16.dp))
            ReUseFilledButton(
                textId = R.string.registration_create_account,
                enabled = state.isRegistrationButtonEnabled && !state.isLoading,
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
            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                text = getAgreementText(),
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.surfaceDim),
            ) {
                sendEvent(RegistrationContract.Event.ShowUserAgreement)
            }
            if (AuthToken.isGoogleSignIn) {
                Spacer(modifier = Modifier.height(24.dp))
                SignInWithGoogle(
                    signInTextResource = R.string.continue_with,
                    enabled = !state.isLoading,
                    onClick = { sendEvent(RegistrationContract.Event.OpenGoogleLoginPage) }
                )
            }
        }
    }
    ReUseAnimatedVisibility(visible = state.isLoading) {
        ReUseProgressDialog()
    }
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
        val isPasswordMismatch =
            state.error is RegistrationContract.RegistrationError.PasswordsMismatch

        ReUseOutlinedTextField(
            value = state.email,
            placeholder = R.string.login_email,
            onValueChanged = { sendEvent(RegistrationContract.Event.UpdateEmail(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            enabled = !state.isLoading,
            isError = state.isErrorEmail,
            errorTextId = when (state.error) {
                is RegistrationContract.RegistrationError.UserEmailExists -> R.string.user_email_exists
                is RegistrationContract.RegistrationError.WrongEmailFormat -> R.string.incorrect_email
                else -> null
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        ReUsePasswordOutlinedTextField(
            value = state.password,
            placeholder = R.string.login_password,
            onValueChanged = { sendEvent(RegistrationContract.Event.UpdatePassword(it)) },
            enabled = !state.isLoading,
            isError = state.isErrorPassword,
            errorTextId = if (state.isErrorPassword) R.string.password_format else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
        )
        Spacer(modifier = Modifier.height(4.dp))

        ReUsePasswordOutlinedTextField(
            value = state.confirmationPassword,
            placeholder = R.string.login_password_confirm,
            onValueChanged = { sendEvent(RegistrationContract.Event.UpdateConfirmationPassword(it)) },
            enabled = !state.isLoading,
            isError = isPasswordMismatch,
            errorTextId = if (isPasswordMismatch) R.string.password_mismatch else null,
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 720, heightDp = 1220,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreview"
)
@Preview(
    showBackground = true,
    widthDp = 320, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun StartRegistrationScreenPreview() {
    MyBADTheme {
        StartRegistrationScreen(RegistrationContract.State())
    }
}
