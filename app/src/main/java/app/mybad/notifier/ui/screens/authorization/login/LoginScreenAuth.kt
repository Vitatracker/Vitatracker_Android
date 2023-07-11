package app.mybad.notifier.ui.screens.authorization.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.mybad.notifier.MainActivityViewModel
import app.mybad.notifier.ui.screens.authorization.AuthorizationScreenViewModel
import app.mybad.notifier.ui.screens.authorization.SurfaceSignInWith
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainLoginScreen(
    onBackPressed: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignInClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.sign_in)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressed()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
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
                    onSignInClicked = onSignInClicked
                )
            }
        }
    )
}

@Composable
private fun MainLoginScreen(
    onForgotPasswordClicked: () -> Unit,
    onSignInClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LoginScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            val loginState = remember { mutableStateOf("") }    //{ mutableStateOf("bob@mail.ru") }
            val passwordState = remember { mutableStateOf("") } //{ mutableStateOf("12345678") }

            LoginScreenBaseForSignIn(loginState = loginState, passwordState = passwordState)
            LoginScreenForgotPassword(onForgotPasswordClicked)
            LoginScreenButtonSignIn(onSignInClicked)
            LoginScreenTextPolicy()
            SurfaceSignInWith(
                onClick = {
                    //TODO("проверить для чего updateToken если тут Flow")
//                    mainVM.updateToken()
                }
            )
        }
    }
}

@Composable
private fun LoginScreenBackgroundImage() {
}

@Composable
private fun LoginScreenBaseForSignIn(
    loginState: MutableState<String>,
    passwordState: MutableState<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginScreenEnteredEmail(loginState = loginState)
        LoginScreenEnteredPassword(passwordState = passwordState)
    }
}

@Composable
private fun LoginScreenEnteredEmail(loginState: MutableState<String>) {
    OutlinedTextField(
        value = loginState.value,
        onValueChange = { newLogin -> loginState.value = newLogin },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
            .fillMaxWidth()
            .padding(8.dp),
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
                Pair(
                    Icons.Filled.Visibility,
                    Color.Black
                )
            } else {
                Pair(
                    Icons.Filled.VisibilityOff,
                    Color.Black
                )
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
    ClickableText(
        text = AnnotatedString(stringResource(id = R.string.login_forgot_password)),
        modifier = Modifier
            .padding(start = 30.dp, top = 16.dp),
        onClick = { onForgotPasswordClicked() }
    )
}

@Composable
private fun LoginScreenButtonSignIn(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 45.dp, start = 8.dp, end = 8.dp),
        onClick = { onClick() },
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text = stringResource(id = R.string.sign_in))
    }
}

@Composable
private fun LoginScreenTextPolicy() {
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.login_agree_policy_text),
            modifier = Modifier.padding(top = 40.dp),
            textAlign = TextAlign.Justify
        )
        ClickableText(
            text = AnnotatedString(stringResource(id = R.string.login_text_privacy_policy)),
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
