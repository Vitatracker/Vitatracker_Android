package app.mybad.notifier.ui.screens.authorization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R

@Composable
fun SurfaceSignInWith(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginScreenTextHelp()
        LoginScreenButtonLoginWith(onClick)
    }
}

@Composable
private fun LoginScreenTextHelp() {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_text_recomendation),
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun LoginScreenButtonLoginWith(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        LoginScreenButtonLoginWithFacebook(
            Modifier
                .weight(0.5f)
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp),
            onClick = { onClick() }
        )
        LoginScreenButtonLoginWithGoogle(
            Modifier
                .weight(0.5f)
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp),
            onClick = { onClick() }
        )
    }
}

@Composable
private fun LoginScreenButtonLoginWithFacebook(modifier: Modifier = Modifier, onClick: () -> Unit) {
    ElevatedButton(
        modifier = modifier,
        onClick = { onClick() },
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_email),
            contentDescription = stringResource(id = R.string.facebook),
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSize))
        Text(text = stringResource(id = R.string.facebook))
    }
}

@Composable
private fun LoginScreenButtonLoginWithGoogle(modifier: Modifier = Modifier, onClick: () -> Unit) {
    ElevatedButton(
        modifier = modifier,
        onClick = { onClick() },
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_google),
            contentDescription = stringResource(id = R.string.google),
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSize))
        Text(text = stringResource(id = R.string.google))
    }
}
