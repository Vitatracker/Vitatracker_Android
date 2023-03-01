package app.mybad.notifications

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import javax.inject.Inject

class AlarmService : Service() {

    @Inject lateinit var notificationsScheduler: NotificationsScheduler

    override fun onBind(p0: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val units = this.resources.getStringArray(R.array.units)
        Log.w("AS_", "${intent?.action} \n ${intent?.extras.toString()}")
        if(intent?.getBooleanExtra("reboot", false) == true) {
            notificationsScheduler.rescheduleAll()
        } else {
            val channelId = createNotificationChannel("my_service", "Notifications from MyBAD reminder", applicationContext)
//        val i = Intent(applicationContext, NotActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        val rpi: PendingIntent? = TaskStackBuilder.create(this).run {
//            addNextIntentWithParentStack(i)
//            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//        }
//        val action = NotificationCompat.Action(0, "Open app", rpi)
            val notification = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.pill)
                .setContentTitle("Time to use drugs!")
//            .addAction(action)
                .setVibrate(longArrayOf(300L,300L,100L,100L,100L,100L,100L))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText("Use your ${intent?.getStringExtra("medName")} : ${intent?.getIntExtra("dose", 0)}, ${units[intent?.getIntExtra("unit", 0) ?: 0]}!")
                .setCategory(Notification.CATEGORY_CALL)
                .build()
            startForeground(101, notification)
            stopForeground(STOP_FOREGROUND_DETACH)
        }
        return START_STICKY
    }

    private fun createNotificationChannel(channelId: String, channelName: String, context: Context): String {
        val c = NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(channelName)
            .setDescription("Alarms from notifier")
            .setVibrationEnabled(true)
            .setLightsEnabled(true)
            .setShowBadge(true)
            .build()
        val serviceC = NotificationManagerCompat.from(context)
        serviceC.createNotificationChannel(c)
        return channelId
    }
}