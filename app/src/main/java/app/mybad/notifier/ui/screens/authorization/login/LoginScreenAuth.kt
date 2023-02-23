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
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.SurfaceSignInWith

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainLoginScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.sign_in)) },
                navigationIcon = {
                    IconButton(onClick = {
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                MainLoginScreen()
            }
        })

}

@Composable
fun MainLoginScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LoginScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            LoginScreenBaseForSignIn()
            LoginScreenForgotPassword()
            LoginScreenButtonSignIn()
            LoginScreenTextPolicy()
            SurfaceSignInWith(onClick = { /*TODO*/ })
        }
    }

}

@Composable
fun LoginScreenBackgroundImage() {



}

@Composable
fun LoginScreenBaseForSignIn() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginScreenEnteredEmail()
        LoginScreenEnteredPassword()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenEnteredEmail() {
    var loginState by remember { mutableStateOf("") }

    OutlinedTextField(
        value = loginState,
        onValueChange = { newLogin -> loginState = newLogin },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenEnteredPassword() {
    var passwordState by remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState,
        onValueChange = { newPassword -> passwordState = newPassword },
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
fun LoginScreenForgotPassword() {
    ClickableText(
        text = AnnotatedString(stringResource(id = R.string.login_forgot_password)),
        modifier = Modifier
            .padding(start = 30.dp, top = 16.dp),
        onClick = { /*TODO*/ }
    )
}

@Composable
fun LoginScreenButtonSignIn() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 45.dp, start = 8.dp, end = 8.dp),
        onClick = { /*TODO*/ },
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text = stringResource(id = R.string.sign_in))
    }
}

@Composable
fun LoginScreenTextPolicy() {
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

