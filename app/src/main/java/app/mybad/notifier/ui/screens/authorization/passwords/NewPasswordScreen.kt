package app.mybad.notifier.ui.screens.authorization.passwords

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R

@Composable
fun StartNewPasswordScreen(onBackPressed: () -> Unit = {}) {
    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.sign_in,
                onBackPressed = onBackPressed
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                NewPasswordScreen()
            }
        }
    )
}

@Composable
private fun NewPasswordScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
        ) {
            NewPasswordScreenEnteredPassword(R.string.login_password)
            NewPasswordScreenEnteredPassword(R.string.login_password_confirm)
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.text_continue
            ) {
//                navController.navigate(
//                    route = AuthorizationNavItem.Authorization.route
//                )
            }
        }
    }
}

@Composable
private fun NewPasswordScreenEnteredPassword(textId: Int) {
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
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        //TODO("Задать цвет из темы")
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

@Preview
@Composable
fun PreviewNewPasswordScreen() {
    MyBADTheme {
        StartNewPasswordScreen()
    }
}