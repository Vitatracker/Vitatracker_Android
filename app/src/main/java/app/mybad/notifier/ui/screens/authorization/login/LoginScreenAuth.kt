package app.mybad.notifier.ui.screens.authorization.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.screens.reuse.OutlinedPasswordTextField
import app.mybad.notifier.ui.screens.reuse.Progress
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedTextField
import app.mybad.notifier.ui.screens.reuse.SignInWithGoogle
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun StartMainLoginScreen(
    state: LoginScreenContract.State,
    events: Flow<LoginScreenContract.Effect>? = null,
    onEventSent: (event: LoginScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: LoginScreenContract.Effect.Navigation) -> Unit
) {

    LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
        events?.collect { effect ->
            when (effect) {
                is LoginScreenContract.Effect.Navigation.ToForgotPassword -> {
                    onNavigationRequested(LoginScreenContract.Effect.Navigation.ToForgotPassword)
                }

                is LoginScreenContract.Effect.Navigation.ToMain -> {
                    onNavigationRequested(LoginScreenContract.Effect.Navigation.ToMain)
                }

                LoginScreenContract.Effect.Navigation.Back -> {
                    onNavigationRequested(LoginScreenContract.Effect.Navigation.Back)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.sign_in,
                onBackPressed = { onEventSent(LoginScreenContract.Event.ActionBack) }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                MainLoginScreen(
                    onEvent = { onEventSent(it) },
                    state = state
                )
                if (state.isLoading) {
                    Progress()
                }
            }
        }
    )
}

@Composable
private fun MainLoginScreen(
    onEvent: (LoginScreenContract.Event) -> Unit,
    state: LoginScreenContract.State
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(PaddingValues(start = 16.dp, end = 16.dp))
                .fillMaxWidth()
        ) {
            LoginScreenEnteredEmail(
                login = state.email,
                updateLogin = { onEvent(LoginScreenContract.Event.UpdateLogin(it)) },
                isError = state.isError
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginScreenEnteredPassword(
                password = state.password,
                updatePassword = { onEvent(LoginScreenContract.Event.UpdatePassword(it)) },
                isError = state.isError,
                errorTextId = if (state.isError) R.string.incorrect_credentials else null
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginScreenForgotPassword { onEvent(LoginScreenContract.Event.ForgotPassword) }
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.sign_in,
                onClick = { onEvent(LoginScreenContract.Event.SignIn(state.email, state.password)) },
                isEnabled = state.isLoginEnabled
            )
            Spacer(modifier = Modifier.height(32.dp))
            SignInWithGoogle { onEvent(LoginScreenContract.Event.SignInWithGoogle) }
        }
    }
}

@Composable
private fun LoginScreenEnteredEmail(login: String, updateLogin: (String) -> Unit, isError: Boolean) {
    ReUseOutlinedTextField(
        value = login,
        label = stringResource(id = R.string.login_email),
        onValueChanged = { newLogin -> updateLogin(newLogin) },
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = Next
        )
    )
}

@Composable
private fun LoginScreenEnteredPassword(password: String, updatePassword: (String) -> Unit, isError: Boolean, errorTextId: Int?) {
    OutlinedPasswordTextField(
        value = password,
        label = stringResource(id = R.string.login_password),
        onValueChanged = { newPassword -> updatePassword(newPassword) },
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
