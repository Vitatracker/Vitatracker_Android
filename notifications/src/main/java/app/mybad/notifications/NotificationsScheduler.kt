package app.mybad.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationsScheduler @Inject constructor(
    private val context: Context,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo
){
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val calendar = Calendar.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)
    fun setAlarm(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val i = Intent(context, AlarmReceiver::class.java)
            val med = medsRepo.getSingle(it.medId)
            i.putExtra("medName", med.name ?: "no name")
            i.putExtra("dose", med.details.dose)
            i.putExtra("unit", med.details.measureUnit)
            val pi = PendingIntent.getBroadcast(context, 0, i, 0)
            calendar.timeInMillis = it.useTime*1000
            Log.w("NS_", "scheduled at ${System.currentTimeMillis()/1000} for ${calendar.timeInMillis/1000}")
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
        }
    }

    fun rescheduleAll() {
        val i = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, System.currentTimeMillis().toInt(), i, 0)
        val now = System.currentTimeMillis()/1000
        scope.launch {
            usagesRepo.getCommonAll().forEach {
                if(it.useTime >= now) {
                    Log.w("NS_", "scheduled at ${System.currentTimeMillis()/1000} for ${calendar.timeInMillis/1000}")
                    calendar.timeInMillis = it.useTime*1000
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
                }
            }
        }.invokeOnCompletion {
            Toast.makeText(context, "items had been rescheduled!", Toast.LENGTH_SHORT).show()
        }
    }
}