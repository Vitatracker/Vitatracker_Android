package app.mybad.notifier.ui.screens.settings.notifications_request

import android.content.res.Configuration
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.common.ReUseAlertDialog
import app.mybad.notifier.ui.common.ReUseAnimatedVisibility
import app.mybad.notifier.ui.common.ReUseTwoButtons
import app.mybad.notifier.ui.theme.MyBADTheme
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
                .fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextView()
            Spacer(modifier = Modifier.height(32.dp))
            ReUseTwoButtons(
                modifier = Modifier
                    .fillMaxWidth(),
                dismissId = R.string.settings_notifications_allow_button_next,
                onDismiss = { sendEvent(NotificationRequestContract.Event.OnReject) },
                confirmId = R.string.settings_notifications_allow_button_settings,
                onConfirm = { sendEvent(NotificationRequestContract.Event.OnSettings) },
            )
        }
    }
    ReUseAnimatedVisibility(state.confirmation) {
        ReUseAlertDialog(
            titleId = R.string.settings_notifications_allow_reconfirmation_title,
            textId = R.string.settings_notifications_allow_reconfirmation_text,
            firstId = R.string.settings_notifications_allow_reconfirmation_button_dismiss,
            onClickFirst = { sendEvent(NotificationRequestContract.Event.OnNext) },
            secondId = R.string.settings_notifications_allow_reconfirmation_button_confirm,
            onClickSecond = { sendEvent(NotificationRequestContract.Event.OnSettings) },
        )
    }
}

@Composable
private fun TextView() {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(id = R.string.settings_notifications_allow_title),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(id = R.string.settings_notifications_allow_text),
        style = MaterialTheme.typography.bodyLarge,
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
