package app.mybad.notifier.ui.screens.authorization.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseOutlinedButton
import app.mybad.notifier.ui.common.SignInWithGoogle
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Preview
@Composable
fun StartAuthorizationScreen(
    modifier: Modifier = Modifier,
    effectFlow: Flow<AuthorizationContract.Effect>? = null,
    sendEvent: (event: AuthorizationContract.Event) -> Unit = {},
    navigation: (navigationEffect: AuthorizationContract.Effect.Navigation) -> Unit = {},
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is AuthorizationContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AuthorizationScreen(
            sendEvent = sendEvent,
        )
    }
}

@Composable
fun AuthorizationScreen(
    modifier: Modifier = Modifier,
    sendEvent: (event: AuthorizationContract.Event) -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AuthorizationImage()
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                ReUseFilledButton(
                    textId = R.string.authorization_screen_login,
                    onClick = { sendEvent(AuthorizationContract.Event.SignIn) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ReUseOutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    textId = R.string.authorization_screen_registration,
                    onClick = { sendEvent(AuthorizationContract.Event.Registration) }
                )
                Spacer(modifier = Modifier.height(32.dp))
                SignInWithGoogle(
                    onClick = { sendEvent(AuthorizationContract.Event.SignInWithGoogle) }
                )
            }
        }
    }
}

@Composable
private fun AuthorizationImage() {
    Image(
        modifier = Modifier
            .size(350.dp),
        imageVector = ImageVector.vectorResource(id = R.drawable.enter_screen),
        contentDescription = null,
        contentScale = ContentScale.Fit
    )
}
