package app.mybad.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("AR_", "${intent?.action}")
        when(intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                val intentService = Intent(context, RescheduleAlarmService::class.java)
                context?.startForegroundService(intentService)
            }
            "schedule.ADD_MED" -> {
                val i = Intent(context, AlarmService::class.java)
                i.putExtra("medName", intent.getStringExtra("medName") ?: "no name")
                i.putExtra("dose", intent.getIntExtra("dose", 0))
                i.putExtra("type", intent.getIntExtra("unit", 0))
                context?.startService(i)
            }
        }
    }
}