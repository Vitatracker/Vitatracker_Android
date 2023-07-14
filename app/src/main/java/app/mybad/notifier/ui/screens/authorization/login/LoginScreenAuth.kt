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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.SignInWithGoogle
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainLoginScreen(
    onBackPressed: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = true) {
        viewModel.event.collect {
            when (it) {
                LoginScreenEvents.InvalidCredentials -> {

                }

                LoginScreenEvents.LoginSuccessful -> {
                    onLoginSuccess()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TitleText(textStringRes = R.string.sign_in)
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.navigation_back))
                    }
                }
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
                    onForgotPasswordClicked = onForgotPasswordClicked,
                    onSignInClicked = viewModel::signIn,
                    onSignInWithGoogleClicked = viewModel::signInWithGoogle
                )
            }
        }
    )
}

@Composable
private fun MainLoginScreen(
    onForgotPasswordClicked: () -> Unit,
    onSignInClicked: (String, String) -> Unit,
    onSignInWithGoogleClicked: () -> Unit
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
            val loginState = remember { mutableStateOf("") }    //{ mutableStateOf("bob@mail.ru") }
            val passwordState = remember { mutableStateOf("") } //{ mutableStateOf("12345678") }
            LoginScreenEnteredEmail(loginState = loginState)
            Spacer(modifier = Modifier.height(16.dp))
            LoginScreenEnteredPassword(passwordState = passwordState)
            Spacer(modifier = Modifier.height(16.dp))
            LoginScreenForgotPassword(onForgotPasswordClicked)
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(textId = R.string.sign_in) {
                onSignInClicked(loginState.value, passwordState.value)
            }
            Spacer(modifier = Modifier.height(32.dp))
            SignInWithGoogle(onClick = onSignInWithGoogleClicked)
        }
    }
}

@Composable
private fun LoginScreenEnteredEmail(loginState: MutableState<String>) {
    OutlinedTextField(
        value = loginState.value,
        onValueChange = { newLogin -> loginState.value = newLogin },
        modifier = Modifier.fillMaxWidth(),
        enabled = true,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_email)) },
        placeholder = { Text(text = stringResource(id = R.string.login_email)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = Next
        )
    )
}

@Composable
private fun LoginScreenEnteredPassword(passwordState: MutableState<String>) {
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { newPassword -> passwordState.value = newPassword },
        modifier = Modifier
            .fillMaxWidth(),
        enabled = true,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_password)) },
        placeholder = { Text(text = stringResource(id = R.string.login_password)) },
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
        text = AnnotatedString(stringResource(id = R.string.login_forgot_password))
    )
}
