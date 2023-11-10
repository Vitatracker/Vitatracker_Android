package app.mybad.notifier.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun StartSplashScreen(
    state: SplashScreenContract.State,
    effectFlow: Flow<ViewSideEffect>? = null,
    sendEvent: (event: SplashScreenContract.Event) -> Unit = {},
    navigation: (navigationEffect: SplashScreenContract.Effect.Navigation) -> Unit = {}
) {

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SplashScreenContract.Effect.Navigation -> navigation(effect)
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageView()
            NewUserGreeting(
                isButtonVisible = state.startButtonVisible,
                onBeginClicked = { sendEvent(SplashScreenContract.Event.OnAuthorization) }
            )
        }
    }
}

@Composable
private fun ImageView() {
    Image(
        modifier = Modifier
            .fillMaxWidth(),
        painter = painterResource(R.drawable.welcome),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun NewUserGreeting(isButtonVisible: Boolean, onBeginClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                append(stringResource(R.string.splash_welcome_in))
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(stringResource(id = R.string.app_name))
                }
            },
            style = Typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.splash_welcome_text),
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(visible = isButtonVisible) {
            ReUseFilledButton(
                textId = R.string.action_begin,
                onClick = onBeginClicked
            )
        }
    }
}

@Preview
@Composable
fun StartSplashScreenPreview() {
    StartSplashScreen(SplashScreenContract.State(startButtonVisible = true))
}
