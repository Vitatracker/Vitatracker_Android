package app.mybad.notifier.ui.screens.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import app.mybad.theme.R
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedButton

@Composable
fun StartAuthorizationScreen(
    onLoginButtonClicked: () -> Unit,
    onRegistrationButtonClicked: () -> Unit,
    onSignInWithGoogleClicked: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        MainAuthorizationScreen(
            onLoginButtonClicked = onLoginButtonClicked,
            onRegistrationButtonClicked = onRegistrationButtonClicked,
            onSignInWithGoogleClicked = onSignInWithGoogleClicked
        )
    }
}

@Composable
fun MainAuthorizationScreen(
    onLoginButtonClicked: () -> Unit,
    onRegistrationButtonClicked: () -> Unit,
    onSignInWithGoogleClicked: () -> Unit
) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            AuthorizationScreenImage()
            ReUseFilledButton(textId = R.string.authorization_screen_login, onLoginButtonClicked)
            Spacer(modifier = Modifier.height(16.dp))
            ReUseOutlinedButton(R.string.authorization_screen_registration, onRegistrationButtonClicked)
            Spacer(modifier = Modifier.height(32.dp))
            SignInWithGoogle(onClick = { onSignInWithGoogleClicked() })
        }
    }
}

@Composable
private fun AuthorizationScreenImage() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_man_doctor),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

