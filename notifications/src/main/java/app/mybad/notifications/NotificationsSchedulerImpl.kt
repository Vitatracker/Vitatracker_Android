package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import app.mybad.domain.models.med.MedDomainModel
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

    override suspend fun add(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val med = medsRepo.getSingle(it.medId)
            val pi = generatePi(med, it, context)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.useTime*1000L, pi)
        }
    }

    override suspend fun cancel(usages: List<UsageCommonDomainModel>) {
        usages.forEach {
            val med = medsRepo.getSingle(it.medId)
            val pi = generatePi(med, it, context)
            alarmManager.cancel(pi)
            pi.cancel()
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
                val med = medsRepo.getSingle(it.medId)
                val pi = generatePi(med, it, context)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, it.useTime*1000, pi)
            }
        }
        onComplete()
    }

    private fun generatePi(med: MedDomainModel, usage: UsageCommonDomainModel, context: Context) : PendingIntent {
        val i = Intent(context.applicationContext, AlarmReceiver::class.java)
        i.action = NotificationIntent
        i.data = Uri.parse("custom://${(usage.useTime + med.id).toInt()}")
        i.putExtra(Extras.MED_NAME.name, med.name ?: "no name")
        i.putExtra(Extras.MED_ID.name, med.id)
        i.putExtra(Extras.TYPE.name, med.type)
        i.putExtra(Extras.ICON.name, med.icon)
        i.putExtra(Extras.COLOR.name, med.color)
        i.putExtra(Extras.DOSE.name, med.dose)
        i.putExtra(Extras.UNIT.name, med.measureUnit)
        i.putExtra(Extras.USAGE_TIME.name, usage.useTime)
        i.putExtra(Extras.QUANTITY.name, usage.quantity)
        return PendingIntent.getBroadcast(context, (usage.useTime + med.id).toInt(), i, 0)
    }
    companion object {
        const val NotificationIntent = "android.intent.action.NOTIFICATION"
        enum class Extras {
            MED_ID,
            MED_NAME,
            TYPE,
            ICON,
            COLOR,
            DOSE,
            UNIT,
            USAGE_TIME,
            QUANTITY,
            FACT_TIME
        }
    }
}