package app.mybad.notifier.ui.screens.authorization.registration

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.AuthorizationScreenViewModel
import app.mybad.notifier.ui.screens.authorization.SurfaceSignInWith
import app.mybad.notifier.ui.screens.authorization.login.*
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainRegistrationScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel
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
                MainRegistrationScreen(navController = navController, authVM = authVM)
            }
        }
    )
}

@Composable
private fun MainRegistrationScreen(
    navController: NavHostController,
    authVM: AuthorizationScreenViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        RegistrationScreenBackgroundImage()
        Column(
            modifier = Modifier
        ) {
            RegistrationScreenBaseForSignIn()
            RegistrationScreenButtonRegistration()
            Spacer(modifier = Modifier.height(30.dp))
            SurfaceSignInWith(onClick = { /*TODO*/ })
        }
    }
}

@Composable
private fun RegistrationScreenBackgroundImage() {
}

@Composable
private fun RegistrationScreenBaseForSignIn() {
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
private fun RegistrationScreenEnteredEmail() {
    var loginState by remember { mutableStateOf("") }

    OutlinedTextField(
        value = loginState,
        shape = MaterialTheme.shapes.small,
        onValueChange = { newLogin -> loginState = newLogin },
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
private fun RegistrationScreenEnteredPassword(textId: Int) {
    var passwordState by remember { mutableStateOf("") }
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState,
        shape = MaterialTheme.shapes.small,
        onValueChange = { newPassword -> passwordState = newPassword },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        enabled = true,
        singleLine = true,
        label = { Text(text = stringResource(id = textId), color = Color.LightGray) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = Done
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
private fun RegistrationScreenButtonRegistration() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 8.dp, end = 8.dp),
        onClick = { },
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
