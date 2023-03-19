package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.Extras

@SuppressLint("UnspecifiedImmutableFlag")
class AlarmService : Service() {

    companion object {
        const val CHANNEL_ID = "my_service"
        const val CHANNEL_NAME = "Notifications from MyBAD reminder"
        const val TAKE_INTENT = "android.intent.action.TAKE_INTENT"
        const val DELAY_INTENT = "android.intent.action.DELAY_INTENT"
        const val FORCE_CLOSE = "android.intent.action.FORCE_CLOSE"
    }

    override fun onBind(p0: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action == FORCE_CLOSE) stopSelf()

        val types = resources.getStringArray(R.array.types)
//        val colors = resources.getIntArray(R.array.colors)
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
        val type = intent?.getIntExtra(Extras.TYPE.name, 0) ?: 0
        val qty = intent?.getIntExtra(Extras.QUANTITY.name, 0)
        val name = intent?.getStringExtra(Extras.MED_NAME.name)
//        val medId = intent?.getLongExtra(Extras.MED_ID.name, 0L)
//        val color = intent?.getIntExtra(Extras.COLOR.name, 0) ?: 0
//        val icon = intent?.getIntExtra(Extras.ICON.name, 0) ?: 0

        val contentText = String.format(baseContext.getString(R.string.notifications_text_template), name, qty, types[type])
        val takeIntent = Intent(baseContext, AlarmReceiver::class.java).apply {
            action = TAKE_INTENT
            putExtra(Extras.MED_ID.name, intent?.getLongExtra(Extras.MED_ID.name, 0L))
            putExtra(Extras.USAGE_TIME.name, intent?.getLongExtra(Extras.USAGE_TIME.name, 0L))
        }
//        val delayIntent = Intent(baseContext, AlarmReceiver::class.java).apply {
//            action = DELAY_INTENT
//            putExtra(Extras.MED_NAME.name, intent?.getStringExtra(Extras.MED_NAME.name))
//            putExtra(Extras.MED_ID.name, intent?.getLongExtra(Extras.MED_ID.name, 0L))
//            putExtra(Extras.TYPE.name, intent?.getIntExtra(Extras.TYPE.name, 0))
//            putExtra(Extras.ICON.name, intent?.getIntExtra(Extras.ICON.name, 0))
//            putExtra(Extras.COLOR.name, intent?.getIntExtra(Extras.COLOR.name, 0))
//            putExtra(Extras.DOSE.name, intent?.getIntExtra(Extras.DOSE.name, 0))
//            putExtra(Extras.UNIT.name, intent?.getIntExtra(Extras.UNIT.name, 0))
//            putExtra(Extras.USAGE_TIME.name, intent?.getLongExtra(Extras.USAGE_TIME.name, 0L))
//            putExtra(Extras.QUANTITY.name, intent?.getIntExtra(Extras.QUANTITY.name, 0))
//        }
        val takePi = PendingIntent.getBroadcast(baseContext, 0, takeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val delayPi = PendingIntent.getBroadcast(baseContext, 0, delayIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.main_icon)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(Notification.CATEGORY_CALL)
            .setContentText(contentText)
            .setContentTitle(baseContext.getString(R.string.notifications_time_to_use))
            .addAction(0, resources.getString(R.string.notifications_action_take), takePi)
//            .addAction(0, resources.getString(R.string.notifications_action_delay), delayPi)
            .build()
        startForeground(1, notification)
        icons.recycle()
        return START_STICKY
    }

}