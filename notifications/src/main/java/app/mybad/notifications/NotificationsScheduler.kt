package app.mybad.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import app.mybad.domain.models.usages.UsageCommonDomainModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class NotificationsScheduler @Inject constructor(
    private val context: Context
){
    fun setAlarm(usages: List<UsageCommonDomainModel>) {
        val i = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        usages.forEach {
            Log.w("NS_", "scheduled for ${it.medId} : ${it.useTime}")
            Log.w("NS_", "now: ${System.currentTimeMillis()/1000}")
            calendar.timeInMillis = it.useTime*1000
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
        }
    }
}