package app.mybad.notifier.ui.screens.settings.notifications_request

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Preview
@Composable
fun NotificationRequestScreen(
    effectFlow: Flow<ViewSideEffect>? = null,
    sendEvent: (event: NotificationRequestContract.Event) -> Unit = {},
    navigation: (navigationEffect: NotificationRequestContract.Effect.Navigation) -> Unit = {}
) {

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is NotificationRequestContract.Effect.Navigation -> navigation(effect)
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextView()
                Spacer(modifier = Modifier.height(32.dp))
                ButtonView(
                    onRejectClicked = { sendEvent(NotificationRequestContract.Event.OnNext) },
                    onAllowClicked = { sendEvent(NotificationRequestContract.Event.OnSettings) },
                )
            }
        }
    }
}

@Composable
private fun ButtonView(onRejectClicked: () -> Unit, onAllowClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ReUseFilledButton(
            modifier = Modifier.weight(0.5f),
            textId = R.string.settings_notifications_allow_button_next,
            onClick = onRejectClicked,
        )
        Spacer(modifier = Modifier.width(16.dp))
        ReUseFilledButton(
            modifier = Modifier.weight(0.5f),
            textId = R.string.settings_notifications_allow_button_settings,
            onClick = onAllowClicked,
        )
    }
}

@Composable
private fun TextView() {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(id = R.string.settings_notifications_allow_title),
        style = Typography.titleMedium,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(id = R.string.settings_notifications_allow_text),
        style = Typography.bodyLarge,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ImageView() {
    Image(
        modifier = Modifier
            .fillMaxWidth(),
        painter = painterResource(R.drawable.warning),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}
