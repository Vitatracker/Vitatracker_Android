package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.COURSE_NOTIFICATION_INTENT
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.Extras
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.NOTIFICATION_INTENT
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("UnspecifiedImmutableFlag")
class AlarmService : Service() {

    companion object {
        const val CHANNEL_ID = "my_service"
        const val CHANNEL_NAME = "Notifications from Vitatracker reminder"
        const val TAKE_INTENT = "android.intent.action.TAKE_INTENT"
        const val DELAY_INTENT = "android.intent.action.DELAY_INTENT"
        const val FORCE_CLOSE = "android.intent.action.FORCE_CLOSE"
    }

    override fun onBind(p0: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val types = resources.getStringArray(R.array.types)
        val units = resources.getStringArray(R.array.units)
        val icons = resources.obtainTypedArray(R.array.icons)
        val channel = NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(CHANNEL_NAME)
            .setDescription(baseContext.getString(R.string.notifications_channel_description))
            .setVibrationEnabled(true)
            .setVibrationPattern(longArrayOf(300L,300L,100L,300L,300L,300L, 100L, 300L))
            .setLightsEnabled(true)
            .setShowBadge(true)
            .build()
        NotificationManagerCompat.from(applicationContext).createNotificationChannel(channel)
        when(intent?.action) {
            FORCE_CLOSE -> stopSelf()
            NOTIFICATION_INTENT -> {
                val type = intent.getIntExtra(Extras.TYPE.name, 0)
                val qty = intent.getIntExtra(Extras.QUANTITY.name, 0)
                val name = intent.getStringExtra(Extras.MED_NAME.name)
                val contentText = String.format(baseContext.getString(R.string.notifications_text_template), name, qty, types[type])
                val takeIntent = Intent(baseContext, AlarmReceiver::class.java).apply {
                    action = TAKE_INTENT
                    putExtra(Extras.MED_ID.name, intent.getLongExtra(Extras.MED_ID.name, 0L))
                    putExtra(Extras.USAGE_TIME.name, intent.getLongExtra(Extras.USAGE_TIME.name, 0L))
                }
                val takePi = PendingIntent.getBroadcast(baseContext, 0, takeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.main_icon)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(Notification.CATEGORY_CALL)
                    .setContentText(contentText)
                    .setContentTitle(baseContext.getString(R.string.notifications_time_to_use))
                    .addAction(0, resources.getString(R.string.notifications_action_take), takePi)
                    .build()
                startForeground(1, notification)
                icons.recycle()
            }
            COURSE_NOTIFICATION_INTENT -> {
                val type = intent.getIntExtra(Extras.TYPE.name, 0)
                val unit = intent.getIntExtra(Extras.UNIT.name, 0)
                val dose = intent.getIntExtra(Extras.DOSE.name, 0)
                val name = intent.getStringExtra(Extras.MED_NAME.name)
                val dateLong = intent.getLongExtra(Extras.NEW_COURSE_START_DATE.name, 0L)
                val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(dateLong), ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
//                    .format(DateTimeFormatter.ISO_LOCAL_DATE)

                val contentText = String.format(baseContext.getString(R.string.notifications_new_course_start_text_template), date, name, dose, units[unit], types[type])
                val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.main_icon)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(Notification.CATEGORY_CALL)
                    .setContentText(contentText)
                    .setContentTitle(baseContext.getString(R.string.notifications_new_course_start))
                    .build()
                startForeground(1, notification)
                stopForeground(STOP_FOREGROUND_DETACH)
                icons.recycle()
            }
        }
        return START_STICKY
    }

}