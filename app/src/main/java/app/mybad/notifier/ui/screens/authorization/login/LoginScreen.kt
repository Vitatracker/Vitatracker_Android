package app.mybad.notifier.ui.screens.authorization.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseOutlinedTextField
import app.mybad.notifier.ui.common.ReUsePasswordOutlinedTextField
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.SignInWithGoogle
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow


@Composable
fun MainLoginScreen(
    state: LoginContract.State,
    effectFlow: Flow<LoginContract.Effect>? = null,
    sendEvent: (event: LoginContract.Event) -> Unit = {},
    navigation: (navigationEffect: LoginContract.Effect.Navigation) -> Unit = {},
) {

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is LoginContract.Effect.Navigation -> navigation(effect)
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
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                LoginScreen(
                    sendEvent = sendEvent,
                    state = state
                )
                if (state.isLoading) {
                    ReUseProgressDialog()
                }
            }
        }
    )
}

@Composable
private fun LoginScreen(
    state: LoginContract.State,
    sendEvent: (LoginContract.Event) -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)

        if (task.isSuccessful) {
            Log.d("VTTAG", "logged isSuccessful")
        } else if (task.isCanceled) {
            Log.d("VTTAG", "logged isCanceled")
        } else if (task.isComplete) {
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

                // Signed in successfully, show authenticated UI.
//                updateUI(account)
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("VTTAG", "signInResult:failed code=" + e.statusCode)
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(PaddingValues(start = 16.dp, end = 16.dp))
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
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
                isEnabled = state.isLoginButtonEnabled && !state.isLoading
            )
            Spacer(modifier = Modifier.height(32.dp))
            SignInWithGoogle(
                enabled = !state.isLoading
            ) {
//                sendEvent(LoginContract.Event.SignInWithGoogle)
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("263428882274-rb8nd9teepapddu2vfkicmhh183t30t0.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
                val gsc = GoogleSignIn.getClient(context, gso);
                
                launcher.launch(gsc.signInIntent)
            }

        }
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
        label = stringResource(id = R.string.login_email),
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
    updatePassword: (String) -> Unit,
    enabled: Boolean,
    isError: Boolean,
    errorTextId: Int?
) {
    ReUsePasswordOutlinedTextField(
        value = password,
        label = stringResource(id = R.string.login_password),
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

@Preview
@Composable
private fun LoginScreenPreview() {
    MyBADTheme {
        LoginScreen(
            state = LoginContract.State(
                email = "preview@google.com",
                password = "1",
                ),
            sendEvent = {}
        )
    }
}
