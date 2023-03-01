package app.mybad.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, AlarmService::class.java)
        i.putExtra("medName", intent?.getStringExtra("medName") ?: "no name")
        i.putExtra("dose", intent?.getIntExtra("dose", 0) ?: 0)
        i.putExtra("unit", intent?.getIntExtra("unit", 0) ?: 0)
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED) i.putExtra("reboot", true)
        context?.startService(i)
    }
}