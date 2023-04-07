package app.mybad.notifications

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmService : Service() {

    @Inject lateinit var notificationsSchedulerImpl: NotificationsSchedulerImpl
    private val scope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val CHANNEL_ID = "my_service"
        const val CHANNEL_NAME = "Notifications from Vitatracker reminder"
    }

    override fun onBind(p0: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val channel = NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(CHANNEL_NAME)
            .setDescription("Alarms from notifier")
            .setVibrationEnabled(true)
            .setLightsEnabled(true)
            .setShowBadge(true)
            .build()
        NotificationManagerCompat.from(applicationContext).createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.pill)
            .setContentTitle("Rescheduling")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentText("Wait until rescheduling will be finished")
            .setCategory(Notification.CATEGORY_CALL)
            .build()
        startForeground(101, notification)
        scope.launch {
            notificationsSchedulerImpl.rescheduleAll()
            stopForeground(STOP_FOREGROUND_DETACH)
        }
        return START_STICKY
    }
}