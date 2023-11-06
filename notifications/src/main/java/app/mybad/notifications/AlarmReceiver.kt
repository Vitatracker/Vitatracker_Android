package app.mybad.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import android.content.Intent.ACTION_DATE_CHANGED
import android.content.Intent.ACTION_TIMEZONE_CHANGED
import android.content.Intent.ACTION_TIME_CHANGED
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.CONTROL_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.COURSE_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.DELAY_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.FORCE_CLOSE
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.RESCHEDULE_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.TAKE_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.USAGE_NOTIFICATION_INTENT
import app.mybad.notifications.service.AlarmService
import app.mybad.notifications.service.RescheduleAlarmService
import app.mybad.notifications.service.TakeOrDelayUsageService
import app.mybad.utils.displayDateTime

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        log("Intent: action=${intent.action}")
        when (intent.action) {
            ACTION_BOOT_COMPLETED, RESCHEDULE_NOTIFICATION_INTENT,
            ACTION_DATE_CHANGED, ACTION_TIMEZONE_CHANGED, ACTION_TIME_CHANGED -> {
                Intent(context, RescheduleAlarmService::class.java)
                    .let(context::startForegroundService)
            }

            TAKE_INTENT, DELAY_INTENT -> {
                val patternId = intent.getLongExtra(Extras.USAGE_ID.name, 0)
                val useTime = intent.getLongExtra(Extras.USAGE_TIME.name, 0)
                log("${intent.action}: patternId=$patternId useTime=${useTime.displayDateTime()}")
                if (patternId > 0 && useTime > 0) {
                    Intent(context, TakeOrDelayUsageService::class.java).apply {
                        action = intent.action
                        data = intent.data
                        putExtra(Extras.USAGE_ID.name, patternId)
                        putExtra(Extras.USAGE_TIME.name, useTime)
                        putExtra(Extras.QUANTITY.name, intent.getFloatExtra(Extras.QUANTITY.name, 1f))

                        putExtra(
                            Extras.REMEDY_NAME.name,
                            intent.getStringExtra(Extras.REMEDY_NAME.name)
                        )
                        putExtra(
                            Extras.REMEDY_ID.name,
                            intent.getLongExtra(Extras.REMEDY_ID.name, 0)
                        )
                        putExtra(Extras.TYPE.name, intent.getIntExtra(Extras.TYPE.name, 0))
                        putExtra(Extras.ICON.name, intent.getIntExtra(Extras.ICON.name, 0))
                        putExtra(Extras.COLOR.name, intent.getIntExtra(Extras.COLOR.name, 0))
                        putExtra(Extras.DOSE.name, intent.getIntExtra(Extras.DOSE.name, 0))
                        putExtra(Extras.UNIT.name, intent.getIntExtra(Extras.UNIT.name, 0))
                    }.let(context::startService)
                } else {
                    clearNotification(context, data = intent.data)
                }
            }

            USAGE_NOTIFICATION_INTENT -> {
                val patternId = intent.getLongExtra(Extras.USAGE_ID.name, 0)
                val useTime = intent.getLongExtra(Extras.USAGE_TIME.name, 0)
                log("${intent.action}: patternId=$patternId useTime=${useTime.displayDateTime()}")
                if (patternId > 0 && useTime > 0) {
                    Intent(context, AlarmService::class.java).apply {
                        action = intent.action
                        data = intent.data
                        putExtra(Extras.USAGE_ID.name, patternId)
                        putExtra(Extras.USAGE_TIME.name, useTime)
                        putExtra(Extras.QUANTITY.name, intent.getFloatExtra(Extras.QUANTITY.name, 1f))

                        putExtra(
                            Extras.REMEDY_NAME.name,
                            intent.getStringExtra(Extras.REMEDY_NAME.name)
                        )
                        putExtra(
                            Extras.REMEDY_ID.name,
                            intent.getLongExtra(Extras.REMEDY_ID.name, 0)
                        )
                        putExtra(Extras.TYPE.name, intent.getIntExtra(Extras.TYPE.name, 0))
                        putExtra(Extras.ICON.name, intent.getIntExtra(Extras.ICON.name, 0))
                        putExtra(Extras.COLOR.name, intent.getIntExtra(Extras.COLOR.name, 0))
                        putExtra(Extras.DOSE.name, intent.getIntExtra(Extras.DOSE.name, 0))
                        putExtra(Extras.UNIT.name, intent.getIntExtra(Extras.UNIT.name, 0))
                    }.let(context::startForegroundService)
                } else {
                    clearNotification(context, data = intent.data)
                }
            }

            COURSE_NOTIFICATION_INTENT -> {
                val courseId = intent.getLongExtra(Extras.COURSE_ID.name, 0)
                val date = intent.getLongExtra(Extras.NEW_COURSE_START_DATE.name, 0)
                log("${intent.action}: patternId=$courseId date=${date.displayDateTime()}")
                if (courseId > 0) {
                    Intent(context, AlarmService::class.java).apply {
                        action = intent.action
                        data = intent.data
                        putExtra(Extras.COURSE_ID.name, courseId)
                        putExtra(Extras.NEW_COURSE_START_DATE.name, date)
                        putExtra(
                            Extras.COURSE_REMIND_TIME.name,
                            intent.getLongExtra(Extras.COURSE_REMIND_TIME.name, 0)
                        )

                        putExtra(
                            Extras.REMEDY_NAME.name,
                            intent.getStringExtra(Extras.REMEDY_NAME.name)
                        )
                        putExtra(
                            Extras.REMEDY_ID.name,
                            intent.getLongExtra(Extras.REMEDY_ID.name, 0)
                        )
                        putExtra(Extras.TYPE.name, intent.getIntExtra(Extras.TYPE.name, 0))
                        putExtra(Extras.ICON.name, intent.getIntExtra(Extras.ICON.name, 0))
                        putExtra(Extras.COLOR.name, intent.getIntExtra(Extras.COLOR.name, 0))
                        putExtra(Extras.DOSE.name, intent.getIntExtra(Extras.DOSE.name, 0))
                        putExtra(Extras.UNIT.name, intent.getIntExtra(Extras.UNIT.name, 0))
                    }.let(context::startForegroundService)
                } else {
                    clearNotification(context, data = intent.data)
                }
            }

            CONTROL_NOTIFICATION_INTENT -> {
                Intent(context, AlarmService::class.java).apply {
                    action = intent.action
                    data = intent.data
                }.let(context::startForegroundService)
            }

            FORCE_CLOSE -> {
                Intent(context, AlarmService::class.java).apply {
                    action = intent.action
                    data = intent.data
                }.let(context::stopService)
            }
        }
    }

    private fun clearNotification(context: Context, data: Uri?) {
        NotificationManagerCompat.from(context).cancel(data?.authority?.toIntOrNull() ?: 0)
    }

    private fun log(text: String, error: Error? = null) {
        if (error == null) {
            Log.w("VTTAG", "NotifiTag::AlarmReceiver::$text")
        } else {
            Log.e("VTTAG", "NotifiTag::AlarmReceiver::$text", error)
        }
    }

}
