package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject
@SuppressLint("UnspecifiedImmutableFlag")
class NotificationsSchedulerImpl
@Inject constructor(
    private val context: Context,
    private val medsRepo: MedsRepo,
    private val usagesRepo: UsagesRepo
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val NotificationIntent = "android.intent.action.NOTIFICATION"
    }

    override suspend fun add(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val i = Intent(context.applicationContext, AlarmReceiver::class.java)
            val med = medsRepo.getSingle(it.medId)
            i.action = NotificationIntent
            i.putExtra("medName", med.name ?: "no name")
            i.putExtra("dose", it.quantity)
            i.putExtra("type", med.type)
            i.data = Uri.fromParts("scheme", "ssp", null)
            val pi = PendingIntent.getBroadcast(context, it.useTime.hashCode() + it.id.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.useTime*1000L, pi)
        }
    }

    override suspend fun cancel(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val i = Intent(context.applicationContext, AlarmReceiver::class.java)
            val med = medsRepo.getSingle(it.medId)
            i.action = NotificationIntent
            i.putExtra("medName", med.name ?: "no name")
            i.putExtra("dose", it.quantity)
            i.putExtra("type", med.type)
            i.data = Uri.fromParts("scheme", "ssp", null)
            val pi = PendingIntent.getBroadcast(context, it.useTime.hashCode() + it.id.hashCode(), i, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.cancel(pi)
        }
    }

    override suspend fun cancelByMedId(medId: Long, onComplete: suspend () -> Unit) {
        val usages = usagesRepo.getUsagesByMedId(medId)
        cancel(usages)
        onComplete()
    }

    suspend fun rescheduleAll(onComplete: () -> Unit = {}) {
        val now = System.currentTimeMillis()/1000
        usagesRepo.getCommonAll().forEach {
            if(it.useTime >= now) {
                val i = Intent(context.applicationContext, AlarmReceiver::class.java)
                val med = medsRepo.getSingle(it.medId)
                i.action = NotificationIntent
                i.putExtra("medName", med.name ?: "no name")
                i.putExtra("dose", it.quantity)
                i.putExtra("type", med.type)
                i.data = Uri.fromParts("scheme", "ssp", null)
                val pi = PendingIntent.getBroadcast(context, it.useTime.hashCode() + it.id.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.useTime*1000, pi)
            }
        }
        onComplete()
    }
}