package app.mybad.notifier.ui.screens.authorization.registration

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.SurfaceSignInWith
import app.mybad.notifier.ui.screens.authorization.login.*

@Composable
fun MainRegistrationScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        RegistrationScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            RegistrationScreenTopBar()
            RegistrationScreenBaseForSignIn()
            RegistrationScreenButtonRegistration()
            Spacer(modifier = Modifier.height(30.dp))
            SurfaceSignInWith(onClick = { /*TODO*/ })
        }
    }

}

@Composable
fun RegistrationScreenBackgroundImage() {

}

@Composable
fun RegistrationScreenTopBar() {

}

@Composable
fun RegistrationScreenBaseForSignIn() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegistrationScreenEnteredEmail()
        RegistrationScreenEnteredPassword(R.string.login_password)
        RegistrationScreenEnteredPassword(R.string.login_password_confirm)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreenEnteredEmail() {
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
fun RegistrationScreenEnteredPassword(textId: Int) {
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
        label = { Text(text = stringResource(id = textId)) },
        placeholder = { Text(text = stringResource(id = textId)) },
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
fun RegistrationScreenButtonRegistration() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 8.dp, end = 8.dp),
        onClick = { /*TODO*/ },
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text = stringResource(id = R.string.text_continue))
    }
}

