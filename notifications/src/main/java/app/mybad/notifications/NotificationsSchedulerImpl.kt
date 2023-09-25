package app.mybad.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.plusDays
import app.mybad.utils.systemToEpochSecond
import app.mybad.utils.systemToInstant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@SuppressLint("UnspecifiedImmutableFlag")
class NotificationsSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remedyRepository: RemedyRepository,
    private val usageRepository: UsageRepository,
    private val courseRepository: CourseRepository,
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun addAlarm(usages: List<UsageDomainModel>) {
        usages.forEach { usage ->
            courseRepository.getCourseById(usage.courseId).getOrNull()?.let { course ->
                remedyRepository.getRemedyById(course.remedyId).getOrNull()?.let { remedy ->
                    val pendingIntent = generateUsagePendingIntent(remedy, usage, context)
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        usage.useTime.systemToInstant().toEpochMilliseconds(),
                        pendingIntent
                    )
                }
            }
        }
    }

    override suspend fun addAlarm(course: CourseDomainModel) {
        course.remindDate?.let { remindDate ->
            remedyRepository.getRemedyById(course.remedyId).getOrNull()?.let { remedy ->
                val pendingIntent = generateCoursePendingIntent(course, remedy, context)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    remindDate.systemToInstant().toEpochMilliseconds(),
                    pendingIntent
                )
            }
        }
    }

    override suspend fun cancelAlarm(usages: List<UsageDomainModel>) {
        usages.forEach { usage ->
            courseRepository.getCourseById(usage.courseId).getOrNull()?.let { course ->
                remedyRepository.getRemedyById(course.remedyId).getOrNull()?.let { remedy ->
                    val pendingIntent = generateUsagePendingIntent(remedy, usage, context)
                    alarmManager.cancel(pendingIntent)
                    pendingIntent.cancel()
                }
            }
        }
    }

    override suspend fun cancelAlarm(course: CourseDomainModel) {
        remedyRepository.getRemedyById(course.remedyId).getOrNull()?.let { remedy ->
            val pendingIntent = generateCoursePendingIntent(course, remedy, context)
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    override suspend fun cancelAlarmByUserId(userId: Long) {
        usageRepository.getUsagesByUserId(userId).getOrNull()?.let { usages ->
            this.cancelAlarm(usages)
        }
    }

    override suspend fun cancelAlarmByCourseId(
        courseId: Long,
        onComplete: suspend () -> Unit
    ) {
        usageRepository.getUsagesByCourseId(courseId).getOrNull()?.let { usages ->
            this.cancelAlarm(usages)
            onComplete()
        }
    }

    override suspend fun rescheduleAlarmByUserId(userId: Long, onComplete: () -> Unit) {
        val now = currentDateTimeSystem()
        usageRepository.getUsagesByUserId(userId).getOrNull()?.forEach { usage ->
            if (usage.useTime >= now) {
                courseRepository.getCourseById(usage.courseId).getOrNull()?.let { course ->
                    remedyRepository.getRemedyById(course.remedyId).getOrNull()?.let { remedy ->
                        val pendingIntent = generateUsagePendingIntent(remedy, usage, context)
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            usage.useTime.systemToInstant().toEpochMilliseconds(),
                            pendingIntent
                        )
                    }
                }
            }
        }
        courseRepository.getCoursesByUserId(userId).getOrNull()?.forEach { course ->
            course.remindDate?.takeIf { it >= now }?.let { remindDate ->
                remedyRepository.getRemedyById(course.remedyId).getOrNull()?.let { remedy ->
                    val pendingIntent = generateCoursePendingIntent(course, remedy, context)
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        remindDate.systemToInstant().toEpochMilliseconds(),
                        pendingIntent
                    )
                }
            }
        }
        onComplete()
    }

    private fun generateUsagePendingIntent(
        remedy: RemedyDomainModel,
        usage: UsageDomainModel,
        context: Context
    ): PendingIntent {
        val useTime = usage.useTime.systemToEpochSecond()
        val i = Intent(context.applicationContext, AlarmReceiver::class.java)
        i.action = NOTIFICATION_INTENT
        i.data = Uri.parse("custom://${(useTime + remedy.id).toInt()}")
        i.putExtra(Extras.REMEDY_NAME.name, remedy.name ?: "no name")
        i.putExtra(Extras.REMEDY_ID.name, remedy.id)
        i.putExtra(Extras.TYPE.name, remedy.type)
        i.putExtra(Extras.ICON.name, remedy.icon)
        i.putExtra(Extras.COLOR.name, remedy.color)
        i.putExtra(Extras.DOSE.name, remedy.dose)
        i.putExtra(Extras.UNIT.name, remedy.measureUnit)
        i.putExtra(Extras.USAGE_TIME.name, useTime)
        i.putExtra(Extras.QUANTITY.name, usage.quantity)
        return PendingIntent.getBroadcast(
            context,
            (useTime + remedy.id).toInt(),
            i,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        )
    }

    private fun generateCoursePendingIntent(
        course: CourseDomainModel,
        remedy: RemedyDomainModel,
        context: Context
    ): PendingIntent {
        val startDate = course.endDate.plusDays(course.interval.toInt()).systemToEpochSecond()
        val remindDate = course.remindDate?.systemToEpochSecond() ?: 0
        val i = Intent(context.applicationContext, AlarmReceiver::class.java)
        i.action = COURSE_NOTIFICATION_INTENT
        i.data = Uri.parse("custom://${(course.id * 1000 + remedy.id).toInt()}")
        i.putExtra(Extras.REMEDY_NAME.name, remedy.name ?: "no name")
        i.putExtra(Extras.REMEDY_ID.name, remedy.id)
        i.putExtra(Extras.TYPE.name, remedy.type)
        i.putExtra(Extras.ICON.name, remedy.icon)
        i.putExtra(Extras.COLOR.name, remedy.color)
        i.putExtra(Extras.DOSE.name, remedy.dose)
        i.putExtra(Extras.UNIT.name, remedy.measureUnit)
        i.putExtra(Extras.NEW_COURSE_START_DATE.name, startDate)
        i.putExtra(Extras.COURSE_REMIND_TIME.name, remindDate)
        return PendingIntent.getBroadcast(
            context,
            (remindDate + remedy.id).toInt(),
            i,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        )
    }

    companion object {
        const val NOTIFICATION_INTENT = "android.intent.action.NOTIFICATION"
        const val COURSE_NOTIFICATION_INTENT = "android.intent.action.COURSE_NOTIFICATION"

        enum class Extras {
            REMEDY_ID,
            REMEDY_NAME,
            TYPE,
            ICON,
            COLOR,
            DOSE,
            UNIT,
            USAGE_TIME,
            QUANTITY,
            COURSE_REMIND_TIME,
            NEW_COURSE_START_DATE,
        }
    }
}
