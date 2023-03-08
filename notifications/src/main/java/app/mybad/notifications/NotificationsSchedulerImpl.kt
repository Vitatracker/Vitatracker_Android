package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationsSchedulerImpl @Inject constructor(
    private val context: Context,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo
) : NotificationsScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val calendar = Calendar.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun add(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            scope.launch {
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
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun cancel(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            scope.launch {
                val i = Intent(context, AlarmReceiver::class.java)
                val med = medsRepo.getSingle(it.medId)
                i.putExtra("medName", med.name ?: "no name")
                i.putExtra("dose", med.dose)
                i.putExtra("unit", med.measureUnit)
                val pi = PendingIntent.getBroadcast(context, 0, i, 0)
                alarmManager.cancel(pi)
            }
        }
    }

    override fun cancelByMedId(medId: Long) {
        scope.launch {
            val usages = usagesRepo.getUsagesByMedId(medId)
            cancel(usages)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun rescheduleAll(onComplete: () -> Unit = {}) {
        val now = System.currentTimeMillis()/1000
        scope.launch {
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
        }.invokeOnCompletion {
            onComplete()
        }
    }
}