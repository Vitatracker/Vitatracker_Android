package app.mybad.notifier.ui.screens.authorization.registration

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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.mybad.notifier.MainActivityViewModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.AuthorizationScreenViewModel
import app.mybad.notifier.ui.screens.authorization.SurfaceSignInWith
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainRegistrationScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel,
    mainVM: MainActivityViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.sign_in)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(route = AuthorizationNavItem.Authorization.route)
                        }
                    ) {
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
                MainRegistrationScreen(
                    navController = navController,
                    authVM = authVM,
                    mainVM = mainVM
                )
            }
        }
    )
}

@Composable
private fun MainRegistrationScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel,
    mainVM: MainActivityViewModel
) {
    val userNameState = remember { mutableStateOf("") }
    val loginState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        RegistrationScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            RegistrationScreenBaseForSignIn(
                loginState = loginState,
                passwordState = passwordState,
                userNameState = userNameState,
                confirmPasswordState = confirmPasswordState
            )
            RegistrationScreenButtonRegistration(
                onClick = {
                    authVM.registration(
                        login = loginState.value,
                        password = passwordState.value,
                        userName = userNameState.value
                    )
                    // TODO("Проверить зачем тут updateToken, если он Flow, при изменении обновиться автоматически")
//                    delay(1200)
//                    mainVM.updateToken()
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
            SurfaceSignInWith(
                onClick = {
                    // TODO("Проверить зачем тут updateToken, если он Flow")
//                mainVM.updateToken()
                }
            )
        }
    }
}

@Composable
private fun RegistrationScreenBackgroundImage() {
}

@Composable
private fun RegistrationScreenBaseForSignIn(
    loginState: MutableState<String>,
    passwordState: MutableState<String>,
    confirmPasswordState: MutableState<String>,
    userNameState: MutableState<String>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegistrationScreenEnteredEmail(loginState = loginState)
        RegistrationScreenEnteredName(nameState = userNameState)
        RegistrationScreenEnteredPassword(
            passwordState = passwordState,
            textId = R.string.login_password
        )
        RegistrationScreenEnteredPassword(
            passwordState = confirmPasswordState,
            textId = R.string.login_password_confirm
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationScreenEnteredName(nameState: MutableState<String>) {
    OutlinedTextField(
        value = nameState.value,
        shape = MaterialTheme.shapes.small,
        onValueChange = { newName -> nameState.value = newName },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        enabled = true,
        singleLine = true,
        label = {
            Text(
                text = stringResource(id = R.string.settings_user_name),
                color = Color.LightGray
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = Next
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationScreenEnteredEmail(loginState: MutableState<String>) {
    OutlinedTextField(
        value = loginState.value,
        shape = MaterialTheme.shapes.small,
        onValueChange = { newLogin -> loginState.value = newLogin },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        enabled = true,
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.login_email), color = Color.LightGray) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = Next
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationScreenEnteredPassword(passwordState: MutableState<String>, textId: Int) {
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState.value,
        shape = MaterialTheme.shapes.small,
        onValueChange = { newPassword -> passwordState.value = newPassword },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        enabled = true,
        singleLine = true,
        label = { Text(text = stringResource(id = textId), color = Color.LightGray) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = Next
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer
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
private fun RegistrationScreenButtonRegistration(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 8.dp, end = 8.dp),
        onClick = { onClick() },
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = stringResource(id = R.string.text_continue),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
