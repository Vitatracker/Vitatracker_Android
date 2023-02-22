package app.mybad.notifier.ui.screens.authorization.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R

@Composable
fun LoginScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LoginScreenBackgroundImage()
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            LoginScreenTopBar()
            LoginScreenBaseForSignIn()
        }
    }

}

@Composable
fun LoginScreenBackgroundImage() {

}

@Composable
fun LoginScreenTopBar() {

}

@Composable
fun LoginScreenBaseForSignIn() {
    val passwordState = remember { TextFieldValue("") }

    Column(
        modifier = Modifier.fillMaxSize(),
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
            imeAction = Send
        ),
//        keyboardActions = ,
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
