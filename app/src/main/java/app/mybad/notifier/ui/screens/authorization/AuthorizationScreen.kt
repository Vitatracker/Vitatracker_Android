package app.mybad.notifier.ui.screens.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.R

@Composable
fun MainAuthorizationScreen(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        AuthorizationScreenBackgroundImage(modifier.fillMaxSize())
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AuthorizationScreenImage()
            AuthorizationScreenButtonEntry()
            AuthorizationScreenTextHelp()
            AuthorizationScreenButtonLoginWith()
        }
    }

}

@Composable
fun AuthorizationScreenBackgroundImage(modifier: Modifier = Modifier) {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_background_authorization_screen),
        contentDescription = null,
        modifier = modifier,
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun AuthorizationScreenImage(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .padding(30.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_man_doctor),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun AuthorizationScreenButtonEntry(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthorizationScreenButtonLogin()
        AuthorizationScreenButtonRegistration()
    }
}

@Composable
fun AuthorizationScreenButtonLogin(modifier: Modifier = Modifier) {
    Button(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        onClick = { /*TODO*/ },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_login),
            modifier = modifier,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AuthorizationScreenButtonRegistration(modifier: Modifier = Modifier) {
    ElevatedButton(
        modifier = modifier
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
            .fillMaxWidth(),
        onClick = { /*TODO*/ },
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_registration),
            modifier = modifier,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AuthorizationScreenTextHelp(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.authorization_screen_text_recomendation),
            modifier = modifier.padding(top = 16.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun AuthorizationScreenButtonLoginWith(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        AuthorizationScreenButtonLoginWithFacebook(
            modifier
                .weight(0.5f)
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp)
        )
        AuthorizationScreenButtonLoginWithGoogle(
            modifier
                .weight(0.5f)
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp)
        )
    }
}

@Composable
fun AuthorizationScreenButtonLoginWithFacebook(modifier: Modifier = Modifier) {
    ElevatedButton(
        modifier = modifier,
        onClick = { /*TODO*/ },
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
fun AuthorizationScreenButtonLoginWithGoogle(modifier: Modifier = Modifier) {
    ElevatedButton(
        modifier = modifier,
        onClick = { /*TODO*/ },
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    MainAuthorizationScreen()
}