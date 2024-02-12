package app.mybad.notifier.ui.screens.authorization.login

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun MainLoginScreen(
    state: LoginContract.State,
    effectFlow: Flow<LoginContract.Effect>? = null,
    sendEvent: (event: LoginContract.Event) -> Unit = {},
    navigation: (navigationEffect: LoginContract.Effect.Navigation) -> Unit = {},
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {activity->
        activity.data?.let { intent ->
            val exception = AuthorizationException.fromIntent(intent)
            if (exception == null && activity.resultCode == Activity.RESULT_OK) {
                Log.w("VTTAG","LoginScreen|AuthFragment|extractTokenCallback get")
                AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest()?.let {
                    sendEvent(LoginContract.Event.TokenExchange(it))
                } ?: context.showToast("Error: TokenExchange is null")
            } else {
                Log.w("VTTAG","LoginScreen|AuthFragment|extractTokenCallback resultCode=${activity.resultCode}")
                context.showToast("Error: Authorization - ${exception?.localizedMessage}")
            }
        }

        sendEvent(LoginContract.Event.SignInWithGoogle)
    }
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is LoginContract.Effect.Navigation -> navigation(effect)
                is LoginContract.Effect.OpenAuthPage -> {
                    Log.w("VTTAG","LoginScreen|AuthFragment|extractTokenCallback launcher")
                    if (effect.intent.resolveActivity(context.packageManager) != null) {
                        launcher.launch(effect.intent)
                    } else {
                        Log.w("VTTAG","LoginScreen|AuthFragment|extractTokenCallback launcher error")
                        context.showToast("Error: intent is not resolveActivity!")
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.sign_in,
                onBackPressed = { sendEvent(LoginContract.Event.ActionBack) }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LoginScreen(
                state = state,
                sendEvent = sendEvent,
            )
            ReUseAnimatedVisibility(visible = state.isLoading) {
                ReUseProgressDialog()
            }
        }
    }
}

@Composable
private fun LoginScreen(
    state: LoginContract.State,
    sendEvent: (LoginContract.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        LoginScreenEnteredEmail(
            login = state.email,
            updateLogin = { sendEvent(LoginContract.Event.UpdateLogin(it)) },
            enabled = !state.isLoading,
            isError = state.isErrorEmail
        )
        Spacer(modifier = Modifier.height(8.dp))
        LoginScreenEnteredPassword(
            password = state.password,
            updatePassword = { sendEvent(LoginContract.Event.UpdatePassword(it)) },
            enabled = !state.isLoading,
            isError = state.isErrorPassword,
            errorTextId = if (state.isError) R.string.incorrect_credentials else null
        )
        Spacer(modifier = Modifier.height(8.dp))
        LoginScreenForgotPassword { sendEvent(LoginContract.Event.ForgotPassword) }
        Spacer(modifier = Modifier.height(32.dp))
        ReUseFilledButton(
            textId = R.string.sign_in,
            onClick = { sendEvent(LoginContract.Event.SignIn(state.email, state.password)) },
            enabled = state.isLoginButtonEnabled && !state.isLoading
        )
        Spacer(modifier = Modifier.height(32.dp))
        SignInWithGoogle(
            enabled = true,
//            enabled = !state.isLoading,
            onClick = {
                Log.w(
                    "VTTAG",
                    "LoginScreen|AuthFragment|extractTokenCallback sendEvent OpenGoogleLoginPage"
                )
                sendEvent(LoginContract.Event.OpenGoogleLoginPage)
            }
        )
    }
}

@Composable
private fun LoginScreenEnteredEmail(
    login: String,
    updateLogin: (String) -> Unit,
    enabled: Boolean,
    isError: Boolean
) {
    ReUseOutlinedTextField(
        value = login,
        placeholder = R.string.login_email,
        onValueChanged = updateLogin::invoke,
        enabled = enabled,
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun LoginScreenEnteredPassword(
    password: String,
    enabled: Boolean,
    isError: Boolean,
    errorTextId: Int?,
    updatePassword: (String) -> Unit,
) {
    ReUsePasswordOutlinedTextField(
        value = password,
        placeholder = R.string.login_password,
        onValueChanged = updatePassword::invoke,
        enabled = enabled,
        isError = isError,
        errorTextId = errorTextId
    )
}

@Composable
private fun LoginScreenForgotPassword(onForgotPasswordClicked: () -> Unit) {
    Text(
        modifier = Modifier.clickable { onForgotPasswordClicked() },
        text = stringResource(id = R.string.login_forgot_password)
    )
}

@Preview(
    showBackground = true,
    widthDp = 1080, heightDp = 1920,
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
fun MainLoginScreenPreview() {
    MyBADTheme {
        MainLoginScreen(LoginContract.State())
    }
}
