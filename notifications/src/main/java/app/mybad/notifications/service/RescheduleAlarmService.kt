package app.mybad.notifications.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.notifications.channel.NotificationInfoChannel
import app.mybad.theme.R
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

    @Inject
    lateinit var notificationsScheduler: NotificationsScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var isServiceStarted = false

    private val notificationBuilder by lazy {
        NotificationCompat.Builder(applicationContext, NotificationInfoChannel.CHANNEL_ID)
            .setContentTitle(resources.getString(R.string.notifications_reschedule_title))
            .setGroup(resources.getString(R.string.notifications_reschedule_title))
            .setGroupSummary(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSilent(true)
            .setCategory(Notification.CATEGORY_EVENT)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.pill)
    }

    override fun onBind(p0: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        super.onStartCommand(intent, flags, startId)
        if (!isServiceStarted) process()
        return START_STICKY
    }

    private fun process() {
        isServiceStarted = true
        Log.w("VTTAG", "NotifiTag::RescheduleAlarmService: process")
        startForeground(NOTIFICATION_ID, getNotification(R.string.notifications_reschedule_text))
        scope.launch {
            // может не быть активного пользователя, но тут будет проверка если пользователь залогинен
            notificationsScheduler.rescheduleAlarm()
            stopForeground(STOP_FOREGROUND_REMOVE)
            isServiceStarted = false
            stopSelf()
        }
    }

    private fun getNotification(@StringRes textId: Int) = notificationBuilder
        .setContentText(getString(textId))
        .build()

    override fun onDestroy() {
        if (scope.isActive) scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val NOTIFICATION_ID = 145102
    }
}
