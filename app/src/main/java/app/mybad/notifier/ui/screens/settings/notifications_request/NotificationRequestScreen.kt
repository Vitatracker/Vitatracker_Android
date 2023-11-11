package app.mybad.notifier.ui.screens.settings.notifications_request

import android.content.res.Configuration
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
import app.mybad.notifier.ui.common.ReUseAlertDialog
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun NotificationRequestScreen(
    state: NotificationRequestContract.State,
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
                onRejectClicked = { sendEvent(NotificationRequestContract.Event.OnReject) },
                onAllowClicked = { sendEvent(NotificationRequestContract.Event.OnSettings) },
            )
        }
        if (state.isConfirmation) {
            ReUseAlertDialog(
                titleId = R.string.settings_notifications_allow_reconfirmation_title,
                descriptionId = R.string.settings_notifications_allow_reconfirmation_text,
                dismissId = R.string.settings_notifications_allow_reconfirmation_button_dismiss,
                confirmId = R.string.settings_notifications_allow_reconfirmation_button_confirm,
                onDismiss = { sendEvent(NotificationRequestContract.Event.OnNext) },
                onConfirm = { sendEvent(NotificationRequestContract.Event.OnSettings) },
            )
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
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.FillWidth
    )
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
fun NotificationRequestScreenPreview() {
    MyBADTheme {
        NotificationRequestScreen(state = NotificationRequestContract.State(true))
    }
}
