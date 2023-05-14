package app.mybad.notifier.ui.screens.authorization.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.mybad.notifier.MainActivityViewModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.AuthorizationScreenViewModel
import app.mybad.notifier.ui.screens.authorization.SurfaceSignInWith
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainLoginScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel,
    mainVM: MainActivityViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.sign_in)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(route = AuthorizationNavItem.Authorization.route)
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
                MainLoginScreen(navController = navController, authVM = authVM, mainVM = mainVM)
            }
        }
    )
}

@Composable
private fun MainLoginScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel,
    mainVM: MainActivityViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LoginScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            val loginState = remember { mutableStateOf("bob@mail.ru") }
            val passwordState = remember { mutableStateOf("12345678") }

            LoginScreenBaseForSignIn(loginState = loginState, passwordState = passwordState)
            LoginScreenForgotPassword(navController = navController)
            LoginScreenButtonSignIn(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        authVM.logIn(
                            login = loginState.value,
                            password = passwordState.value
                        )
                        delay(1200)
                        mainVM.updateToken()
                    }
                }
            )
            LoginScreenTextPolicy()
            SurfaceSignInWith(onClick = { mainVM.updateToken() })
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
private fun LoginScreenForgotPassword(navController: NavHostController) {
    ClickableText(
        text = AnnotatedString(stringResource(id = R.string.login_forgot_password)),
        modifier = Modifier
            .padding(start = 30.dp, top = 16.dp),
        onClick = { navController.navigate(route = AuthorizationNavItem.RecoveryPassword.route) }
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
