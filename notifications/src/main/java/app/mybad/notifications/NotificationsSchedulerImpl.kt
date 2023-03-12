package app.mybad.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class NotificationsSchedulerImpl @Inject constructor(
    private val context: Context,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun add(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val i = Intent(context, AlarmReceiver::class.java)
            val med = medsRepo.getSingle(it.medId)
            i.action = "schedule.ADD_MED"
            i.putExtra("medName", med.name ?: "no name")
            i.putExtra("dose", med.dose)
            i.putExtra("type", med.type)
            val t = LocalDateTime.ofInstant(Instant.ofEpochSecond(it.useTime), ZoneOffset.UTC)
            val pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, t.toEpochSecond(ZoneOffset.UTC)*1000, pi)
        }
    }

    override suspend fun cancel(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val i = Intent(context, AlarmReceiver::class.java)
            val med = medsRepo.getSingle(it.medId)
            i.action = "schedule.ADD_MED"
            i.putExtra("medName", med.name ?: "no name")
            i.putExtra("dose", med.dose)
            i.putExtra("unit", med.measureUnit)
            val pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pi)
        }
    }

    override suspend fun cancelByMedId(medId: Long) {
        val usages = usagesRepo.getUsagesByMedId(medId)
        cancel(usages)
    }

    suspend fun rescheduleAll(onComplete: () -> Unit = {}) {
        val now = System.currentTimeMillis()/1000
        usagesRepo.getCommonAll().forEach {
            if(it.useTime >= now) {
                val i = Intent(context, AlarmReceiver::class.java)
                val med = medsRepo.getSingle(it.medId)
                i.action = "schedule.ADD_MED"
                i.putExtra("medName", med.name ?: "no name")
                i.putExtra("dose", med.dose)
                i.putExtra("unit", med.measureUnit)
                val pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, it.useTime*1000, pi)
            }
        }
        onComplete()
    }
}