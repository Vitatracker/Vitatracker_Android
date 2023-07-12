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
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMainRecoveryPasswordScreenAuth(
    onBackPressed: () -> Unit,
    onContinueClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { TitleText(textStringRes = R.string.password_recovery) },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressed()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.navigation_back))
                    }
                }
            )
        }) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            MainRecoveryPasswordScreenAuth(onContinueClicked)
        }
    }
}

@Composable
private fun MainRecoveryPasswordScreenAuth(onContinueClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.fillMaxWidth(), text = stringResource(id = R.string.password_recovery_enter_mail), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))
        RecoveryPasswordScreenTextEmail()
        Spacer(modifier = Modifier.height(32.dp))
        ReUseFilledButton(textId = R.string.text_continue) {
            onContinueClicked()
        }
    }
}

@Composable
private fun RecoveryPasswordScreenTextEmail() {
    var loginState by remember { mutableStateOf("") }

    OutlinedTextField(
        value = loginState,
        onValueChange = { newLogin -> loginState = newLogin },
        modifier = Modifier
            .fillMaxWidth(),
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
