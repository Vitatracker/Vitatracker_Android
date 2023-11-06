package app.mybad.notifier.utils

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat


fun Context.isNotification() = NotificationManagerCompat.from(this).areNotificationsEnabled()

fun Context.notificationChannel(channelId: String) = NotificationManagerCompat.from(this)
    .getNotificationChannel(channelId)

fun Context.openAppSettings(settings: String, channelId: String? = null, isData: Boolean = false) {
    try {
        Intent().apply {
            // для настроек каналов или настроек
            action = channelId?.let(::notificationChannel)?.let { channel ->
                putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
            } ?: settings
            if (isData) {
                // используется при запросе разрешений и вызывается какой-то параметр настройки
                data = Uri.parse("package:${packageName}")
            } else {
                // вызываются настройки для приложения
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
        }.let(::startActivity)
    } catch (e: ActivityNotFoundException) {
        // Обрабатываем если экрана нет
    }
}

fun Context.canAlarms() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    alarmManager?.canScheduleExactAlarms() ?: false
} else true

val Context.alarmManager: AlarmManager?
    get() = getSystemService(AlarmManager::class.java)

val Context.powerManager: PowerManager?
    get() = getSystemService(PowerManager::class.java)

val Context.notificationManager: NotificationManager?
    get() = getSystemService(NotificationManager::class.java)
