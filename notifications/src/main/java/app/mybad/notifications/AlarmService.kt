package app.mybad.notifications

import android.app.Notification
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import javax.inject.Inject

class AlarmService : Service() {

    @Inject lateinit var notificationsSchedulerImpl: NotificationsSchedulerImpl

    companion object {
        const val CHANNEL_ID = "my_service"
        const val CHANNEL_NAME = "Notifications from MyBAD reminder"
    }

    override fun onBind(p0: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val types = this.resources.getStringArray(R.array.types)
        val c = NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(CHANNEL_NAME)
            .setDescription("Alarms from notifier")
            .setVibrationEnabled(true)
            .setLightsEnabled(true)
            .setShowBadge(true)
            .build()
        NotificationManagerCompat.from(applicationContext).createNotificationChannel(c)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.pill)
            .setContentTitle("Time to use drugs!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentText("Use your ${intent?.getStringExtra("medName")} : ${intent?.getIntExtra("dose", 0)}, ${types[intent?.getIntExtra("type", 0) ?: 0]}!")
            .setCategory(Notification.CATEGORY_CALL)
            .build()
        startForeground(1, notification)
        stopForeground(STOP_FOREGROUND_DETACH)
        return START_STICKY
    }
}