package app.mybad.notifier.ui.screens.authorization.start

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseImage
import app.mybad.notifier.ui.common.ReUseOutlinedButton
import app.mybad.notifier.ui.common.SignInWithGoogle
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun AuthorizationScreen(
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
    ) {
        AuthorizationView(sendEvent)
    }
}

@Composable
fun AuthorizationView(
    sendEvent: (event: AuthorizationContract.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        ReUseImage(
            imageVector = R.drawable.enter_screen,
            modifier = Modifier
                .fillMaxSize(0.87f)
                .padding(bottom = 42.dp)
                .weight(1f)
        )
        Column {
            ReUseFilledButton(
                textId = R.string.authorization_screen_login,
                onClick = { sendEvent(AuthorizationContract.Event.SignIn) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReUseOutlinedButton(
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

@Preview(
    showBackground = true,
    widthDp = 1080, heightDp = 1920,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreview"
)
@Preview(
    showBackground = true,
    widthDp = 320, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun StartAuthorizationScreenPreview() {
    MyBADTheme {
        AuthorizationScreen()
    }
}
