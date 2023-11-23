package app.mybad.notifications.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.CONTROL_NOTIFICATION_ID
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.CONTROL_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.COURSE_NOTIFICATION_ID
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.COURSE_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.DELAY_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.FORCE_CLOSE
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.TAKE_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.USAGE_NOTIFICATION_ID
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.USAGE_NOTIFICATION_INTENT
import app.mybad.notifications.AlarmReceiver
import app.mybad.notifications.Extras
import app.mybad.notifications.channel.NotificationTrackerChannel
import app.mybad.theme.R
import app.mybad.utils.displayDateTime
import app.mybad.utils.displayTime
import app.mybad.utils.toText

@SuppressLint("UnspecifiedImmutableFlag")
class AlarmService : Service() {

    private val types by lazy(LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.types) }
    private val units by lazy(LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.units) }
    private val icons by lazy(LazyThreadSafetyMode.NONE) { resources.obtainTypedArray(R.array.icons) }

    override fun onBind(p0: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand: start")
        intent?.let(::process)
        return START_NOT_STICKY
    }

    private fun process(intent: Intent) {
        log("Intent: action=${intent.action}")
        val notification = when (intent.action) {
            FORCE_CLOSE -> {
                stopSelf()
                null
            }

            USAGE_NOTIFICATION_INTENT -> showUsageNotification(intent)

            COURSE_NOTIFICATION_INTENT -> showCourseNotification(intent)

            CONTROL_NOTIFICATION_INTENT -> showControlNotification(intent)

            else -> null
        }
        notification?.let {
            log("Intent: notification id=${it.first}")
            startForegroundWithType(it.first, it.second)
        }
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun startForegroundWithType(id: Int, notification:Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(id, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE)
        } else {
            startForeground(id, notification)
        }
    }

    private fun showUsageNotification(intent: Intent): Pair<Int, Notification> {
        val icon = intent.getIntExtra(Extras.ICON.name, 0)
        val type = intent.getIntExtra(Extras.TYPE.name, 0)
        val quantity = intent.getFloatExtra(Extras.QUANTITY.name, 1f)
        val name = intent.getStringExtra(Extras.REMEDY_NAME.name) ?: ""

        val usageId = intent.getLongExtra(Extras.USAGE_ID.name, 0)
        val time = intent.getLongExtra(Extras.USAGE_TIME.name, 0)

        val contentText = getString(
            R.string.notifications_time_to_use_text,
            name,
            time.displayTime(),
            quantity.toText(),
            types[type],
        )

        log("${intent.action}: $contentText")
        val id = USAGE_NOTIFICATION_ID + usageId.toInt()
        return id to getNotification(
            contentText,
            id = id,
            usageId = usageId,
            time = time,
            icon = icon,
        )
    }

    private fun showCourseNotification(intent: Intent): Pair<Int, Notification> {
        val icon = intent.getIntExtra(Extras.ICON.name, 0)
        val type = intent.getIntExtra(Extras.TYPE.name, 0)
        val unit = intent.getIntExtra(Extras.UNIT.name, 0)
        val dose = intent.getIntExtra(Extras.DOSE.name, 0)
        val courseId = intent.getLongExtra(Extras.COURSE_ID.name, 0)
        val name = intent.getStringExtra(Extras.REMEDY_NAME.name) ?: ""
        val date = intent.getLongExtra(Extras.NEW_COURSE_START_DATE.name, 0)

        val contentText = getString(
            R.string.notifications_new_course_start_text,
            date.displayDateTime(),
            name,
            dose,
            units[unit],
            types[type],
        )

        log("${intent.action}: $contentText")
        val id = COURSE_NOTIFICATION_ID + courseId.toInt()
        return id to getNotification(
            R.string.notifications_new_course_start_title,
            contentText,
            icon,
        )
    }

    private fun showControlNotification(intent: Intent): Pair<Int, Notification> {
        log("${intent.action}: ")
        return CONTROL_NOTIFICATION_ID to getNotification(
            R.string.notifications_control_title,
            R.string.notifications_control_text
        )
    }

    private fun getBuilder() =
        NotificationCompat.Builder(applicationContext, NotificationTrackerChannel.CHANNEL_ID)
            .setGroup(getString(R.string.notifications_group_personal))
//            .setGroupSummary(false)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setSmallIcon(R.drawable.pill)

    private fun getNotification(@StringRes titleId: Int, @StringRes textId: Int) = getBuilder()
        .setContentTitle(getString(titleId))
        .setContentText(getString(textId))
        .build()

    private fun getNotification(@StringRes titleId: Int, text: String, icon: Int) = getBuilder()
        .setContentTitle(getString(titleId))
        .setContentText(text)
        .setLargeIcon(icons.getDrawable(icon)?.toBitmap())
        .build()

    private fun getNotification(text: String, id: Int, usageId: Long, time: Long, icon: Int) =
        getBuilder()
            .setContentTitle(getString(R.string.notifications_time_to_use))
            .setContentText(text)
            .setLargeIcon(icons.getDrawable(icon)?.toBitmap())
            // пауза
            .addAction(
                R.drawable.clock,
                resources.getString(R.string.notifications_action_delay),
                getPendingIntent(id = id, usageId = usageId, time = time, delay = true)
            )
            // применить, выпить, использовать
            .addAction(
                R.drawable.done,
                resources.getString(R.string.notifications_action_take),
                getPendingIntent(id = id, usageId = usageId, time = time)
            )
            .build()

    private fun getPendingIntent(
        id: Int,
        usageId: Long,
        time: Long,
        delay: Boolean = false
    ): PendingIntent {
        val intent = Intent(baseContext, AlarmReceiver::class.java).apply {
            action = if (delay) DELAY_INTENT else TAKE_INTENT
            data = Uri.parse("take://$id")
            putExtra(Extras.USAGE_ID.name, usageId)
            putExtra(Extras.USAGE_TIME.name, time)
        }
        return PendingIntent.getBroadcast(baseContext, id, intent, PendingIntent.FLAG_MUTABLE)
    }

    override fun onDestroy() {
        icons.recycle()
        super.onDestroy()
    }

    private fun log(text: String, error: Error? = null) {
        if (error == null) {
            Log.w("VTTAG", "NotifiTag::AlarmService::$text")
        } else {
            Log.e("VTTAG", "NotifiTag::AlarmService::$text", error)
        }
    }
}
