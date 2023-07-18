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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
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
                LoginScreenContract.Effect.LoginSuccessful -> {}
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
                    onForgotPasswordClicked = { onEventSent(LoginScreenContract.Event.ForgotPassword) },
                    onSignInClicked = { email, password ->
                        onEventSent(LoginScreenContract.Event.LoginWithEmail(email, password))
                    },
                    onSignInWithGoogleClicked = { onEventSent(LoginScreenContract.Event.LoginWithGoogle) },
                    login = state.email,
                    updateLogin = { onEventSent(LoginScreenContract.Event.UpdateLogin(it)) },
                    password = state.password,
                    updatePassword = { onEventSent(LoginScreenContract.Event.UpdatePassword(it)) },
                    isLoggingByEmail = state.isLoggingByEmail
                )
            }
        }
    )
}

@Composable
private fun MainLoginScreen(
    onForgotPasswordClicked: () -> Unit,
    onSignInClicked: (String, String) -> Unit,
    onSignInWithGoogleClicked: () -> Unit,
    login: String,
    password: String,
    updateLogin: (String) -> Unit,
    updatePassword: (String) -> Unit,
    isLoggingByEmail: Boolean
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
            LoginScreenEnteredEmail(login = login, updateLogin = updateLogin)
            Spacer(modifier = Modifier.height(16.dp))
            LoginScreenEnteredPassword(password = password, updatePassword = updatePassword)
            Spacer(modifier = Modifier.height(16.dp))
            LoginScreenForgotPassword(onForgotPasswordClicked)
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(
                textId = R.string.sign_in,
                onClick = {
                    onSignInClicked(login, password)
                },
                isLoading = isLoggingByEmail
            )
            Spacer(modifier = Modifier.height(32.dp))
            SignInWithGoogle(onClick = onSignInWithGoogleClicked)
        }
    }
}

@Composable
private fun LoginScreenEnteredEmail(login: String, updateLogin: (String) -> Unit) {
    OutlinedTextField(
        value = login,
        onValueChange = { newLogin -> updateLogin(newLogin) },
        modifier = Modifier.fillMaxWidth(),
        enabled = true,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_email)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = Next
        )
    )
}

@Composable
private fun LoginScreenEnteredPassword(password: String, updatePassword: (String) -> Unit) {
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { newPassword -> updatePassword(newPassword) },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = true,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_password)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = Done
        ),
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val (icon, iconColor) = if (showPassword.value) {
                Pair(Icons.Filled.Visibility, Color.Black)
            } else {
                Pair(Icons.Filled.VisibilityOff, Color.Black)
            }

            IconButton(onClick = { showPassword.value = !showPassword.value }) {
                Icon(
                    icon,
                    contentDescription = "Visibility",
                    tint = iconColor
                )
            }
        }
    )
}

@Composable
private fun LoginScreenForgotPassword(onForgotPasswordClicked: () -> Unit) {
    Text(
        modifier = Modifier.clickable { onForgotPasswordClicked() },
        text = stringResource(id = R.string.login_forgot_password)
    )
}
