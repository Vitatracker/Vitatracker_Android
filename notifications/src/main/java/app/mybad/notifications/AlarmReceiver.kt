package app.mybad.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("AR_", "${intent?.action}")
        if(Intent.ACTION_BOOT_COMPLETED == intent?.action) {
            Toast.makeText(context, "Rescheduling after reboot", Toast.LENGTH_SHORT).show()
            val intentService = Intent(context, RescheduleAlarmService::class.java)
            context?.startForegroundService(intentService)
        } else {
            val i = Intent(context, AlarmService::class.java)
            i.putExtra("medName", intent?.getStringExtra("medName") ?: "no name")
            i.putExtra("dose", intent?.getIntExtra("dose", 0) ?: 0)
            i.putExtra("unit", intent?.getIntExtra("unit", 0) ?: 0)
            context?.startService(i)
        }
    }
}