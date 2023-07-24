package app.mybad.notifier.ui.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SplashScreen(
    state: SplashScreenContract.State,
    events: Flow<SplashScreenContract.Effect>? = null,
    onEventSent: (event: SplashScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: SplashScreenContract.Effect.Navigation) -> Unit
) {

    LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
        events?.collect {
            when (it) {
                SplashScreenContract.Effect.Navigation.ToAuthorization -> {
                    onNavigationRequested(SplashScreenContract.Effect.Navigation.ToAuthorization)
                }

                SplashScreenContract.Effect.Navigation.ToMain -> {
                    onNavigationRequested(SplashScreenContract.Effect.Navigation.ToMain)
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_background_start_screen),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.start_screen_pills),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                NewUserGreeting(
                    isButtonVisible = state.startButtonVisible,
                    onBeginClicked = { onEventSent(SplashScreenContract.Event.OnStartClicked) }
                )
            }
        }

    }

}

@Composable
private fun NewUserGreeting(isButtonVisible: Boolean, onBeginClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(id = R.string.splash_welcome_text),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(visible = isButtonVisible) {
            ReUseFilledButton(
                textId = R.string.action_begin,
                onClick = { onBeginClicked() }
            )
        }
    }
}