package app.mybad.notifier.ui.screens.settings.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsNotificationsScreen(
    state: SettingsNotificationsContract.State,
    effectFlow: Flow<SettingsNotificationsContract.Effect>? = null,
    sendEvent: (event: SettingsNotificationsContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsNotificationsContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsNotificationsContract.Effect.Navigation -> navigation(effect)

                SettingsNotificationsContract.Effect.CheckSettings -> {}
                SettingsNotificationsContract.Effect.ContactUs -> {}

                SettingsNotificationsContract.Effect.ReloadSettings -> {}
                SettingsNotificationsContract.Effect.SetupNotifications -> {}
                SettingsNotificationsContract.Effect.SetupSleepRegime -> {}
            }
        }
    }
    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.settings_notifications,
                onBackPressed = { sendEvent(SettingsNotificationsContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        val appName: String = stringResource(id = R.string.app_name)
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.settings_notifications_setup_notifications_1),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.settings_notifications_setup_notifications_2),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_notifications_setup_notifications_3),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.settings_notifications_setup_notifications_button,
                onClick = {
                    sendEvent(SettingsNotificationsContract.Event.SetupNotificationsClicked)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.settings_notifications_sleep_regime_1),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_notifications_sleep_regime_2),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.settings_notifications_sleep_regime_button,
                onClick = {
                    sendEvent(SettingsNotificationsContract.Event.SetupSleepRegimeClicked)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.settings_notifications_check_settings_1),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_notifications_check_settings_2),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.settings_notifications_check_settings_button,
                onClick = {
                    sendEvent(SettingsNotificationsContract.Event.CheckSettingsClicked)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.settings_notifications_reload_settings_1),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_notifications_reload_settings_2),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.settings_notifications_reload_settings_button,
                onClick = {
                    sendEvent(SettingsNotificationsContract.Event.ReloadSettingsClicked)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.settings_notifications_contact_us_1),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = String.format(
                    stringResource(R.string.settings_notifications_contact_us_2),
                    appName
                ),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.settings_notifications_contact_us_3),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = String.format(
                    stringResource(R.string.settings_notifications_contact_us_4),
                    appName
                ),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.settings_notifications_contact_us_5),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_notifications_contact_us_6),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.settings_notifications_contact_us_button,
                onClick = {
                    sendEvent(SettingsNotificationsContract.Event.ContactUsClicked)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
