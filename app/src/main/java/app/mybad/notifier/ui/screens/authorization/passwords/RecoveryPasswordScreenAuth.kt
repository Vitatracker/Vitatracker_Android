package app.mybad.notifier.ui.screens.authorization.passwords

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationNavItem
import app.mybad.notifier.ui.screens.reuse.ReUseButtonContinue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainRecoveryPasswordScreenAuth(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.sign_in)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(route = AuthorizationNavItem.Login.route)
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
                MainRecoveryPasswordScreenAuth(navController = navController)
            }
        }
    )
}

@Composable
private fun MainRecoveryPasswordScreenAuth(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        RecoveryPasswordScreenBackgroundImage()
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RecoveryPasswordScreenTextUser()
            Spacer(modifier = Modifier.height(15.dp))
            RecoveryPasswordScreenTextEmail()
            ReUseButtonContinue(textId = R.string.text_continue) {
                navController.navigate(route = AuthorizationNavItem.NewPassword.route)
            }
        }
    }
}

@Composable
private fun RecoveryPasswordScreenBackgroundImage() {
}

@Composable
private fun RecoveryPasswordScreenTextUser() {
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.recovery_text_1), textAlign = TextAlign.Justify)
        Text(text = stringResource(id = R.string.recovery_text_2), textAlign = TextAlign.Justify)
        Text(text = stringResource(id = R.string.recovery_text_3), textAlign = TextAlign.Justify)
    }
}

@Composable
private fun RecoveryPasswordScreenTextEmail() {
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
            imeAction = ImeAction.Next
        )
    )
}
