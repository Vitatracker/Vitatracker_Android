package app.mybad.notifications.channel

import android.app.NotificationChannelGroup
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import app.mybad.theme.R

object NotificationInfoChannel {

    const val CHANNEL_ID = "vitatracker_info"
    private const val CHANNEL_GROUP_ID = "vitatracker_info_group"

    fun create(context: Context) {
        createGroup(context)

        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_LOW
        )
            .setName(context.getString(R.string.notifications_channel_name_info))
            .setDescription(context.getString(R.string.notifications_channel_description_info))
            .setGroup(CHANNEL_GROUP_ID)
            .setVibrationEnabled(false)
            .setLightsEnabled(true)
            .setShowBadge(true)
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    private fun createGroup(context: Context){
        val groupName = context.getString(R.string.notifications_channel_group_personal)
        NotificationManagerCompat.from(context).createNotificationChannelGroup(
            NotificationChannelGroup(CHANNEL_GROUP_ID, groupName)
        )
    }
}
