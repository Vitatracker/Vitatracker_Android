package app.mybad.notifier.ui.screens.authorization.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedButton
import app.mybad.notifier.ui.screens.reuse.SignInWithGoogle
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun StartAuthorizationScreen(
    events: Flow<AuthorizationStartScreenContract.Effect>? = null,
    onEventSent: (event: AuthorizationStartScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: AuthorizationStartScreenContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                AuthorizationStartScreenContract.Effect.Navigation.ToAuthorization -> {
                    onNavigationRequested(AuthorizationStartScreenContract.Effect.Navigation.ToAuthorization)
                }

                AuthorizationStartScreenContract.Effect.Navigation.ToRegistration -> {
                    onNavigationRequested(AuthorizationStartScreenContract.Effect.Navigation.ToRegistration)
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        MainAuthorizationScreen(
            onEventSent = onEventSent
        )
    }
}

@Composable
fun MainAuthorizationScreen(
    onEventSent: (event: AuthorizationStartScreenContract.Event) -> Unit = {}
) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AuthorizationScreenImage()
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                ReUseFilledButton(
                    textId = R.string.authorization_screen_login,
                    onClick = { onEventSent(AuthorizationStartScreenContract.Event.SignIn) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ReUseOutlinedButton(
                    R.string.authorization_screen_registration,
                    onClick = { onEventSent(AuthorizationStartScreenContract.Event.Registration) })
                Spacer(modifier = Modifier.height(32.dp))
                SignInWithGoogle(
                    onClick = { onEventSent(AuthorizationStartScreenContract.Event.SignInWithGoogle) }
                )
            }
        }
    }
}

@Composable
private fun AuthorizationScreenImage() {
    Image(
        modifier = Modifier
            .size(350.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.enter_screen),
        contentDescription = null,
        contentScale = ContentScale.Fit
    )
}

