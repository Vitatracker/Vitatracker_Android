package app.mybad.notifier.ui.screens.settings.notifications

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun SettingsNotificationsScreen(
    effectFlow: Flow<SettingsNotificationsContract.Effect>? = null,
    sendEvent: (event: SettingsNotificationsContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsNotificationsContract.Effect.Navigation) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsNotificationsContract.Effect.Navigation -> navigation(effect)

                SettingsNotificationsContract.Effect.ContactUs -> {}
                SettingsNotificationsContract.Effect.SetupNotifications -> {
                    setupNotifications(context)
                }

                SettingsNotificationsContract.Effect.SetupSleepRegime -> {
                    setupSleepRegime(context)
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
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SetupNotifications(sendEvent)
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
}

@Composable
private fun CheckApps(appName: String) {
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
}

private fun setupNotifications(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Обрабатываем если экрана нет
    }
}

@SuppressLint("BatteryLife")
private fun setupSleepRegime(context: Context) {
    (context.getSystemService() as? PowerManager)?.let { powerManager ->
        if (!powerManager.isIgnoringBatteryOptimizations(context.packageName)) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Обрабатываем если экрана нет
                context.startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            }
        }
    }
}

@Composable
private fun SetupNotifications(sendEvent: (event: SettingsNotificationsContract.Event) -> Unit) {
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
        textId = R.string.settings_notifications_setup_notifications_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.SetupNotificationsClicked)
        }
    )
}

@Composable
private fun SleepMode(sendEvent: (event: SettingsNotificationsContract.Event) -> Unit) {
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
        textId = R.string.settings_notifications_sleep_regime_button,
        onClick = {
            sendEvent(SettingsNotificationsContract.Event.SetupSleepRegimeClicked)
        }
    )
}
