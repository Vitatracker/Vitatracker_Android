package app.mybad.notifications.channel

import android.app.NotificationChannelGroup
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import app.mybad.theme.R

object NotificationTrackerChannel {

    const val CHANNEL_ID = "vitatracker_service"
    private const val CHANNEL_GROUP_ID_PERSONAL = "vitatracker_group_personal" // личные оповещения

    fun create(context: Context) {
        createGroup(context)

        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(context.getString(R.string.notifications_channel_name))
            .setDescription(context.getString(R.string.notifications_channel_description))
            .setGroup(CHANNEL_GROUP_ID_PERSONAL)
            .setVibrationEnabled(true)
            .setVibrationPattern(longArrayOf(300L, 300L, 100L, 300L, 300L, 300L, 100L, 300L))
            .setLightsEnabled(true)
            .setShowBadge(true)
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun createGroup(context: Context) {
        val groupName = context.getString(R.string.notifications_channel_group_personal)
        NotificationManagerCompat.from(context).createNotificationChannelGroup(
            NotificationChannelGroup(CHANNEL_GROUP_ID_PERSONAL, groupName)
        )
    }
}
