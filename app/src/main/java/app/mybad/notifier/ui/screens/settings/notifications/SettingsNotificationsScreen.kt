package app.mybad.notifier.ui.screens.settings.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifications.channel.NotificationInfoChannel
import app.mybad.notifications.channel.NotificationTrackerChannel
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.utils.canAlarms
import app.mybad.notifier.utils.isNotification
import app.mybad.notifier.utils.openAppSettings
import app.mybad.notifier.utils.powerManager
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsNotificationsScreen(
    effectFlow: Flow<SettingsNotificationsContract.Effect>? = null,
    sendEvent: (event: SettingsNotificationsContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsNotificationsContract.Effect.Navigation) -> Unit = {},
) {
    val context = LocalContext.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsNotificationsContract.Effect.Navigation -> navigation(effect)

                SettingsNotificationsContract.Effect.ContactUs -> {}

                SettingsNotificationsContract.Effect.SetupNotifications -> {
                    context.openAppSettings(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                }

                SettingsNotificationsContract.Effect.SetupNotificationsTracker -> {
                    context.openAppSettings(
                        Settings.ACTION_APP_NOTIFICATION_SETTINGS,
                        NotificationTrackerChannel.CHANNEL_ID
                    )
                }

                SettingsNotificationsContract.Effect.SetupNotificationsInfo -> {
                    context.openAppSettings(
                        Settings.ACTION_APP_NOTIFICATION_SETTINGS,
                        NotificationInfoChannel.CHANNEL_ID
                    )
                }

                SettingsNotificationsContract.Effect.SetupAlarms -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        context.openAppSettings(
                            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                            isData = true
                        )
                    }
                }

                SettingsNotificationsContract.Effect.SetupSleepRegime -> {
                    context.setupSleepRegime()
                }
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
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 16.dp
                ),
        ) {
            SetupNotifications(context, sendEvent)
            Spacer(modifier = Modifier.height(32.dp))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                SetupAlarms(context, sendEvent)
            }
            Spacer(modifier = Modifier.height(32.dp))
            SleepMode(sendEvent)
            Spacer(modifier = Modifier.height(32.dp))
            CheckApps(appName)
            Spacer(modifier = Modifier.height(32.dp))
            DonStopForcefully(appName)
            Spacer(modifier = Modifier.height(32.dp))
            ContactUs(sendEvent)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ContactUs(sendEvent: (event: SettingsNotificationsContract.Event) -> Unit) {
    Text(
        text = stringResource(R.string.settings_notifications_contact_us_5),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.settings_notifications_contact_us_6),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(32.dp))
    ReUseFilledButton(
        textId = R.string.settings_notifications_contact_us_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.ContactUsClicked)
        }
    )
}

@Composable
private fun DonStopForcefully(appName: String) {
    Text(
        text = stringResource(R.string.settings_notifications_contact_us_3),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = String.format(
            stringResource(R.string.settings_notifications_contact_us_4),
            appName
        ),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun CheckApps(appName: String) {
    Text(
        text = stringResource(R.string.settings_notifications_contact_us_1),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = String.format(
            stringResource(R.string.settings_notifications_contact_us_2),
            appName
        ),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@SuppressLint("BatteryLife")
private fun Context.setupSleepRegime() {
    val batteryOptimization = powerManager?.isIgnoringBatteryOptimizations(packageName) ?: false
    if (batteryOptimization) {
        openAppSettings(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
    } else {
        openAppSettings(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, isData = true)
    }
}

@Composable
private fun SetupNotifications(
    context: Context,
    sendEvent: (event: SettingsNotificationsContract.Event) -> Unit
) {
    Text(
        text = stringResource(R.string.settings_notifications_setup_notifications_1),
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.height(24.dp))
    val notificationId = if (context.isNotification()) {
        // Уведомления включены для вашего приложения
        R.string.settings_notifications_setup_notifications_2
    } else {
        // Уведомления отключены для вашего приложения
        R.string.settings_notifications_setup_notifications_3
    }
    Text(
        text = stringResource(notificationId),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.settings_notifications_setup_notifications_4),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.height(32.dp))
    ReUseFilledButton(
        textId = R.string.settings_notifications_setup_notifications_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.SetupNotificationsClicked)
        }
    )
    Spacer(modifier = Modifier.height(32.dp))
    ReUseFilledButton(
        textId = R.string.settings_notifications_setup_notifications_tracker_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.SetupNotificationsTrackerClicked)
        }
    )
    Spacer(modifier = Modifier.height(32.dp))
    ReUseFilledButton(
        textId = R.string.settings_notifications_setup_notifications_info_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.SetupNotificationsInfoClicked)
        }
    )
}

@Composable
private fun SetupAlarms(
    context: Context,
    sendEvent: (event: SettingsNotificationsContract.Event) -> Unit
) {
    //TODO("проверить точность проверки параметра")
    val alarmsId = if (context.canAlarms()) {
        // Уведомления включены для вашего приложения
        R.string.settings_notifications_alarms_can
    } else {
        // Уведомления отключены для вашего приложения
        R.string.settings_notifications_alarms_can_not
    }
    Text(
        text = stringResource(alarmsId),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.settings_notifications_alarms),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.height(32.dp))
    ReUseFilledButton(
        textId = R.string.settings_notifications_alarms_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.SetupAlarmsClicked)
        }
    )
}

@Composable
private fun SleepMode(sendEvent: (event: SettingsNotificationsContract.Event) -> Unit) {
    Text(
        text = stringResource(R.string.settings_notifications_sleep_regime_1),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.settings_notifications_sleep_regime_2),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.height(32.dp))
    ReUseFilledButton(
        textId = R.string.settings_notifications_sleep_regime_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.SetupSleepRegimeClicked)
        }
    )
}

@Preview
@Composable
fun SettingsNotificationsScreenPreview() {
    MyBADTheme {
        SettingsNotificationsScreen()
    }
}
