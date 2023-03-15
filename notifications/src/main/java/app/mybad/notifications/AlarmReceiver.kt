package app.mybad.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import app.mybad.notifications.AlarmService.Companion.DELAY_INTENT
import app.mybad.notifications.AlarmService.Companion.FORCE_CLOSE
import app.mybad.notifications.AlarmService.Companion.TAKE_INTENT
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.Extras
import app.mybad.notifications.NotificationsSchedulerImpl.Companion.NotificationIntent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("AR_", "${intent?.action}")
        when(intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                val intentService = Intent(context, RescheduleAlarmService::class.java)
                context?.startForegroundService(intentService)
            }
            NotificationIntent -> {
                val i = Intent(context, AlarmService::class.java)
                i.action = intent.action
                i.putExtra(Extras.MED_NAME.name, intent.getStringExtra(Extras.MED_NAME.name))
                i.putExtra(Extras.MED_ID.name, intent.getLongExtra(Extras.MED_ID.name, 0L))
                i.putExtra(Extras.TYPE.name, intent.getIntExtra(Extras.TYPE.name, 0))
                i.putExtra(Extras.ICON.name, intent.getIntExtra(Extras.ICON.name, 0))
                i.putExtra(Extras.COLOR.name, intent.getIntExtra(Extras.COLOR.name, 0))
                i.putExtra(Extras.DOSE.name, intent.getIntExtra(Extras.DOSE.name, 0))
                i.putExtra(Extras.UNIT.name, intent.getIntExtra(Extras.UNIT.name, 0))
                i.putExtra(Extras.USAGE_TIME.name, intent.getLongExtra(Extras.USAGE_TIME.name, 0L))
                i.putExtra(Extras.QUANTITY.name, intent.getIntExtra(Extras.QUANTITY.name, 0))
                context?.startService(i)
            }
            TAKE_INTENT, DELAY_INTENT -> {
                val i = Intent(context, TakeOrDelayUsageService::class.java)
                val si = Intent(context, AlarmService::class.java)
                si.action = FORCE_CLOSE
                i.action = intent.action
                i.putExtra(Extras.MED_NAME.name, intent.getStringExtra(Extras.MED_NAME.name))
                i.putExtra(Extras.MED_ID.name, intent.getLongExtra(Extras.MED_ID.name, 0L))
                i.putExtra(Extras.TYPE.name, intent.getIntExtra(Extras.TYPE.name, 0))
                i.putExtra(Extras.ICON.name, intent.getIntExtra(Extras.ICON.name, 0))
                i.putExtra(Extras.COLOR.name, intent.getIntExtra(Extras.COLOR.name, 0))
                i.putExtra(Extras.DOSE.name, intent.getIntExtra(Extras.DOSE.name, 0))
                i.putExtra(Extras.UNIT.name, intent.getIntExtra(Extras.UNIT.name, 0))
                i.putExtra(Extras.USAGE_TIME.name, intent.getLongExtra(Extras.USAGE_TIME.name, 0L))
                i.putExtra(Extras.QUANTITY.name, intent.getIntExtra(Extras.QUANTITY.name, 0))
                context?.stopService(si)
                context?.startService(i)
            }
        }
    }
}