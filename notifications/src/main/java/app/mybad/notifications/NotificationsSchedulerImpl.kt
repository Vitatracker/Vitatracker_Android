package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class NotificationsSchedulerImpl @Inject constructor(
    private val context: Context,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val calendar = Calendar.getInstance()
    @SuppressLint("UnspecifiedImmutableFlag")
    override suspend fun add(usages: List<UsageCommonDomainModel>) {
        Log.w("NSI_now", "${LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())}")
        usages.forEach {
            val i = Intent(context, AlarmReceiver::class.java)
            val med = medsRepo.getSingle(it.medId)
            i.putExtra("medName", med.name ?: "no name")
            i.putExtra("dose", med.dose)
            i.putExtra("unit", med.measureUnit)
            val pi = PendingIntent.getBroadcast(context, 0, i, 0)
            calendar.timeInMillis = it.useTime*1000
            Log.w("NSI_datetime", "${LocalDateTime.ofInstant(Instant.ofEpochMilli(calendar.timeInMillis), ZoneId.systemDefault())}")
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override suspend fun cancel(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val i = Intent(context, AlarmReceiver::class.java)
            val med = medsRepo.getSingle(it.medId)
            i.putExtra("medName", med.name ?: "no name")
            i.putExtra("dose", med.dose)
            i.putExtra("unit", med.measureUnit)
            val pi = PendingIntent.getBroadcast(context, 0, i, 0)
            alarmManager.cancel(pi)
        }
    }

    override suspend fun cancelByMedId(medId: Long) {
        val usages = usagesRepo.getUsagesByMedId(medId)
        cancel(usages)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    suspend fun rescheduleAll(onComplete: () -> Unit = {}) {
        val now = System.currentTimeMillis()/1000
        usagesRepo.getCommonAll().forEach {
            if(it.useTime >= now) {
                val i = Intent(context, AlarmReceiver::class.java)
                val med = medsRepo.getSingle(it.medId)
                i.putExtra("medName", med.name ?: "no name")
                i.putExtra("dose", med.dose)
                i.putExtra("unit", med.measureUnit)
                val pi = PendingIntent.getBroadcast(context, 0, i, 0)
                calendar.timeInMillis = it.useTime*1000
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }
        }
        onComplete()
    }
}