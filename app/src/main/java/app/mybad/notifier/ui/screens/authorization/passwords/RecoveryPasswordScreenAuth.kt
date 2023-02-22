package app.mybad.notifier.ui.screens.authorization.passwords

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.reuse.ReUseButtonContinue

@Composable
fun MainRecoveryPasswordScreenAuth() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        RecoveryPasswordScreenBackgroundImage()
        Column(
            modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RecoveryPasswordScreenTopBar()
            RecoveryPasswordScreenTextUser()
            Spacer(modifier = Modifier.height(15.dp))
            RecoveryPasswordScreenTextEmail()
            ReUseButtonContinue(textId = R.string.text_continue) { /*TODO*/ }
        }
    }

}

@Composable
fun RecoveryPasswordScreenBackgroundImage() {

}

@Composable
fun RecoveryPasswordScreenTopBar() {

}

@Composable
fun RecoveryPasswordScreenTextUser() {
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.recovery_text_1), textAlign = TextAlign.Justify)
        Text(text = stringResource(id = R.string.recovery_text_2), textAlign = TextAlign.Justify)
        Text(text = stringResource(id = R.string.recovery_text_3), textAlign = TextAlign.Justify)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryPasswordScreenTextEmail() {
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