package app.mybad.notifier.ui.screens.authorization

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        LoginScreenButtonLoginWith(onClick = onClick)
    }
}

@Composable
private fun LoginScreenTextHelp() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_text_recomendation),
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Justify
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
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSize))
        Text(text = stringResource(id = R.string.facebook), fontSize = 17.sp)
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
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = stringResource(id = R.string.google), fontSize = 17.sp)
    }
}
