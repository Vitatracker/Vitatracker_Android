package app.mybad.notifications

import android.app.Notification
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.mybad.theme.R
import app.mybad.domain.models.AuthToken
import app.mybad.domain.scheduler.NotificationsScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmService : Service() {

    @Inject lateinit var notificationsScheduler: NotificationsScheduler
    // TODO("проверить тут диспатчер")
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    companion object {
        private const val NOTIFICATION_ID = 145101
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

        startForeground(NOTIFICATION_ID, notification)
        scope.launch {
            notificationsScheduler.rescheduleAlarmByUserId(AuthToken.userId)
            stopForeground(STOP_FOREGROUND_DETACH)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        if (scope.isActive) scope.cancel()
    }

}
