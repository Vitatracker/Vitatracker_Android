package app.mybad.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.domain.models.NotificationDomainModel
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.CONTROL_NOTIFICATION_ID
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.CONTROL_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.COURSE_NOTIFICATION_ID
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.COURSE_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.RESCHEDULE_NOTIFICATION_INTENT
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.USAGE_NOTIFICATION_ID
import app.mybad.domain.scheduler.NotificationsScheduler.Companion.USAGE_NOTIFICATION_INTENT
import app.mybad.domain.usecases.courses.CountActiveCourseUseCase
import app.mybad.domain.usecases.courses.GetCourseWithParamsByIdUseCase
import app.mybad.domain.usecases.courses.GetCoursesWithRemindBetweenDateUseCase
import app.mybad.domain.usecases.notification.AddNotificationInDBUseCase
import app.mybad.domain.usecases.notification.DeleteNotificationInDBByIdUseCase
import app.mybad.domain.usecases.notification.DeleteNotificationInDBUseCase
import app.mybad.domain.usecases.notification.GetNotificationInDBUseCase
import app.mybad.domain.usecases.patternusage.GetPatternUsagesWithParamsOnDateUseCase
import app.mybad.domain.usecases.patternusage.GetUsageDisplayByCourseIdUseCase
import app.mybad.domain.usecases.patternusage.GetUsageDisplayByIdUseCase
import app.mybad.domain.usecases.patternusage.SetFactUseTimeAndInsertUsageUseCase
import app.mybad.domain.usecases.user.TakeUserAuthTokenUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationDateUseCase
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.changeTime
import app.mybad.utils.currentDateTimeInMilliseconds
import app.mybad.utils.currentDateTimeInSeconds
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.displayDateTime
import app.mybad.utils.displayTimeInMinutes
import app.mybad.utils.isBetweenDay
import app.mybad.utils.isEqualsDay
import app.mybad.utils.milliSecondsToDisplayDateTime
import app.mybad.utils.minOf
import app.mybad.utils.minusForNotification
import app.mybad.utils.plusDays
import app.mybad.utils.plusSeconds
import app.mybad.utils.systemToEpochSecond
import app.mybad.utils.systemToInstant
import app.mybad.utils.toDateTimeSystem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class NotificationsSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getPatternUsagesUseCase: GetPatternUsagesWithParamsOnDateUseCase,
    private val getUsageDisplayByIdUseCase: GetUsageDisplayByIdUseCase,
    private val getUsageDisplayByCourseIdUseCase: GetUsageDisplayByCourseIdUseCase,
    private val setFactUseTimeAndInsertUsageUseCase: SetFactUseTimeAndInsertUsageUseCase,

    private val addNotificationInDBUseCase: AddNotificationInDBUseCase,
    private val getNotificationInDBUseCase: GetNotificationInDBUseCase,
    private val deleteNotificationInDBByIdUseCase: DeleteNotificationInDBByIdUseCase,
    private val deleteNotificationInDBUseCase: DeleteNotificationInDBUseCase,

    private val getCoursesWithRemindBetweenDateUseCase: GetCoursesWithRemindBetweenDateUseCase,
    private val getCourseWithParamsByIdUseCase: GetCourseWithParamsByIdUseCase,
    private val countActiveCourseUseCase: CountActiveCourseUseCase,

    private val takeUserAuthTokenUseCase: TakeUserAuthTokenUseCase,
    private val updateUserNotificationDateUseCase: UpdateUserNotificationDateUseCase,
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun rescheduleAlarm() {
        if (AuthToken.userId <= 0) takeUserAuthTokenUseCase(currentDateTimeInSeconds())
        rescheduleAlarmByUserId(AuthToken.userId)
    }

    override suspend fun rescheduleAlarmByUserId(userId: Long, onComplete: () -> Unit) {
        log("rescheduleAlarmByUserId: userId=$userId")
        if (userId <= 0) return

        // отменяем все оповещения
        cancelAlarm(userId)

        // добавляем оповещения по приему и курсам на сегодня
        val date = currentDateTimeSystem()
        addAlarmUsages(userId, date)
        addAlarmCourses(userId, date)

        // проверяем что создано хотябы 1 оповещение, если нет, то проверить есть ли активный курс
        if (activeNotificationOrCourse(userId)) addAlarmControlAndReschedule(userId)
        // зафиксируем дату создания оповещений
        updateUserNotificationDateUseCase(userId)

        onComplete()
    }

    private suspend fun activeNotificationOrCourse(userId: Long): Boolean {
        getNotificationInDBUseCase(userId).onSuccess {
            if (it.isNotEmpty()) return true
        }
        countActiveCourseUseCase(userId).onSuccess {
            if (it > 0) return true
        }
        return false
    }

    private suspend fun addAlarmControlAndReschedule(userId: Long) {
        addAlarmControl(userId)
        addAlarmReschedule(userId)
    }

    // Usages
    private suspend fun addAlarmUsages(userId: Long, date: LocalDateTime) {
        getPatternUsagesUseCase(userId, date)?.let { usages ->
            log("addAlarmUsages: usages=${usages.size} date=${date.displayDateTime()}")
            usages.forEach { setAlarm(it, it.useTime) }
        }
    }

    private suspend fun setAlarm(
        usage: UsageDisplayDomainModel,
        notificationTime: LocalDateTime
    ) {
        val time = notificationTime.systemToInstant().toEpochMilliseconds()
        log(
            "setAlarm: usageId=${usage.id} time=${
                notificationTime.displayDateTime()
            } useTime=${
                usage.useTime.displayDateTime()
            } (${usage.timeInMinutes.displayTimeInMinutes()})"
        )
        if (currentDateTimeInMilliseconds() > time) return
        setAlarm(
            userId = usage.userId,
            typeId = usage.id,
            type = NotificationType.PATTERN.ordinal,
            time = time,
            pendingIntent = getPendingIntent(usage)
        )
    }

    override suspend fun addAlarmByPatternUsageId(
        patternId: Long,
        notificationTime: LocalDateTime?
    ) {
        log("addAlarmByPatternUsageId: patternId=$patternId time=${notificationTime?.displayDateTime()}")
        getUsageDisplayByIdUseCase(patternId).getOrNull()?.let { usage ->
            setAlarm(usage, notificationTime ?: usage.useTime)
            // обновим время пересчета для проверки приема и для следющего дня
            addAlarmControlAndReschedule(usage.userId)
        }
    }

    // course
    private suspend fun addAlarmCourses(userId: Long, date: LocalDateTime) {
        getCoursesWithRemindDate(userId, date)?.let { courses ->
            log("setAlarm: courses=${courses.size} time=${date.displayDateTime()}")
            courses.forEach { setAlarm(it, it.remindDate ?: date) }
        }
    }

    private suspend fun getCoursesWithRemindDate(
        userId: Long,
        date: LocalDateTime = currentDateTimeSystem()
    ) = getCoursesWithRemindBetweenDateUseCase(
        userId = userId,
        startTime = date.systemToEpochSecond(),
        endTime = date.atEndOfDay().systemToEpochSecond()
    ).getOrNull()

    private suspend fun setAlarm(course: CourseDisplayDomainModel, date: LocalDateTime) {
        log("setAlarm: courseId=${course.id} time=${date.displayDateTime()}")
        val time = date.systemToInstant().toEpochMilliseconds()
        if (currentDateTimeInMilliseconds() > time) return
        setAlarm(
            userId = course.userId,
            typeId = course.id,
            type = NotificationType.COURSE.ordinal,
            time = time,
            pendingIntent = getPendingIntent(course)
        )
    }

    override suspend fun addAlarmTest() {
        val time = currentDateTimeSystem()
            .plusSeconds(10)
            .systemToInstant().toEpochMilliseconds()
        val userId = AuthToken.userId
        setAlarm(
            userId = userId,
            typeId = 0,
            type = NotificationType.MEDICAL_CONTROL.ordinal,
            time = time,
            pendingIntent = getPendingIntent(userId, time)
        )
    }

    override suspend fun addAlarmByCourseId(courseId: Long) {
        val date = currentDateTimeSystem()
        getCourseWithParamsByIdUseCase(courseId).onSuccess { course ->
            if (date.isBetweenDay(course.startDate, course.endDate)) {
                getUsageDisplayByCourseIdUseCase(courseId = courseId).getOrNull()?.filter {
                    it.withinDayOnRegime(date)
                }?.let { usages ->
                    log("addAlarmByCourseId: courseId=$courseId  date=${date.displayDateTime()} usages=${usages.size}")
                    usages.forEach { setAlarm(it, it.useTime) }
                }
                course.remindDate?.takeIf { it.isEqualsDay(date) }?.let { setAlarm(course, it) }
                // обновим время пересчета для проверки приема и для следющего дня
                addAlarmControlAndReschedule(course.userId)
            } else {
                log(
                    "addAlarmByCourseId: courseId=$courseId date=${
                        date.displayDateTime()
                    } is not startDate=${course.startDate} endDate=${course.endDate}"
                )
            }
        }.onFailure {
            log("addAlarmByCourseId: error: courseId=$courseId date=${date.displayDateTime()}")
        }
    }

    override suspend fun addAlarmControl(userId: Long) {
        var time = currentDateTimeInMilliseconds()
        getNotificationInDBUseCase(userId).getOrNull()?.forEach { notification ->
            if (notification.time > time && notification.time.isEqualsDay(time)) {
                time = notification.time
            }
        }
        time += 10_000 // 10 сек
        log("addAlarmControl: time=${time.milliSecondsToDisplayDateTime()}")
        cancelAlarmControl(userId)
        setAlarm(
            userId = userId,
            typeId = 0,
            type = NotificationType.MEDICAL_CONTROL.ordinal,
            time = time,
            pendingIntent = getPendingIntent(userId, time)
        )
    }

    private suspend fun addAlarmReschedule(userId: Long) {
        val time = getNextDayFirstNotification(userId)
        cancelAlarmReschedule(userId)
        log("addAlarmReschedule: time=${time.displayDateTime()}")
        setAlarm(
            userId = userId,
            typeId = 0,
            type = NotificationType.RESCHEDULE.ordinal,
            time = time.systemToInstant().toEpochMilliseconds(),
            pendingIntent = getPendingIntent(userId, time)
        )
    }

    private suspend fun getNextDayFirstNotification(userId: Long): LocalDateTime {
        val date = currentDateTimeSystem()
            .plusDays() // следующий день
            .changeTime(0, 0)
        val dateCourse = getCoursesWithRemindDate(userId = userId, date = date)
            ?.minOfOrNull { it.remindDate!! }?.minusForNotification()
        val dateUsage = getPatternUsagesUseCase(userId = userId, date = date)
            ?.minOfOrNull { it.useTime }?.minusForNotification()
        val dateMin = date.changeTime(hour = 14, minute = 0).minOf(dateCourse, dateUsage)
        return if (dateMin > date) dateMin else date
    }

    private suspend fun setAlarm(
        userId: Long,
        typeId: Long,
        type: Int,
        time: Long,
        pendingIntent: PendingIntent
    ) {
        log(
            "setAlarm: type=${NotificationType.values()[type]} typeId=$typeId time=${
                time.milliSecondsToDisplayDateTime()
            }"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            log("setAlarm: not canScheduleExactAlarms!!!")
            return
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        addNotificationInDBUseCase(
            NotificationDomainModel(
                userId = userId,
                type = type,
                typeId = typeId,
                date = time.milliSecondsToDisplayDateTime(),
                time = time,
            )
        )
    }

    private fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    override suspend fun cancelAlarm(userId: Long) {
        log("cancelAlarm: all in")
        getNotificationInDBUseCase(userId).getOrNull()?.forEach { notification ->
            val pendingIntent = when (NotificationType.values()[notification.type]) {
                NotificationType.PATTERN -> {
                    cancelNotification(USAGE_NOTIFICATION_ID + notification.typeId.toInt())
                    getUsageDisplayByIdUseCase(notification.typeId).getOrNull()?.let { usage ->
                        getPendingIntent(usage)
                    }
                }

                NotificationType.COURSE -> {
                    cancelNotification(COURSE_NOTIFICATION_ID + notification.typeId.toInt())
                    getCourseWithParamsByIdUseCase(notification.typeId).getOrNull()?.let { course ->
                        getPendingIntent(course)
                    }
                }

                NotificationType.MEDICAL_CONTROL -> {
                    cancelNotification(CONTROL_NOTIFICATION_ID)
                    getPendingIntent(userId, notification.time)
                }

                NotificationType.RESCHEDULE -> {
                    getPendingIntent(userId, notification.time.toDateTimeSystem())
                }
            }
            cancelAlarmAndNotification(pendingIntent, notification.id)
        }
    }

    private suspend fun cancelAlarmAndNotification(
        pendingIntent: PendingIntent?,
        id: Long? = null
    ) {
        try {
//            log("canselAlarmAndNotification: in")
            pendingIntent?.let {
                alarmManager.cancel(it)
                it.cancel()
            }
            id?.let { deleteNotificationInDBByIdUseCase(it) }
        } catch (error: Error) {
            log("canselAlarmAndNotification: error", error)
        }
    }

    private suspend fun cancelAlarmReschedule(userId: Long) {
        log("cancelAlarmReschedule: in")
        getNotificationInDBUseCase(userId).getOrNull()
            ?.firstOrNull { it.type == NotificationType.RESCHEDULE.ordinal }?.let { notification ->
                cancelAlarmAndNotification(
                    getPendingIntent(userId, notification.time.toDateTimeSystem()),
                    notification.id,
                )
            }
    }

    override suspend fun cancelAlarmControl(userId: Long) {
        log("cancelAlarmControl: in")
        getNotificationInDBUseCase(userId).getOrNull()
            ?.firstOrNull { it.type == NotificationType.MEDICAL_CONTROL.ordinal }
            ?.let { notification ->
                cancelNotification(CONTROL_NOTIFICATION_ID)
                cancelAlarmAndNotification(
                    getPendingIntent(userId, notification.time),
                    notification.id,
                )
            }
    }

    override suspend fun cancelAlarmByCourseId(userId: Long, courseId: Long) {
        log("cancelAlarmByCourseId: courseId=$courseId")
        getNotificationInDBUseCase(userId).getOrNull()?.forEach { notification ->
            val pendingIntent = when (NotificationType.values()[notification.type]) {
                NotificationType.PATTERN -> {
                    getUsageDisplayByIdUseCase(notification.typeId).getOrNull()?.let { usage ->
                        if (usage.courseId == courseId) {
                            cancelNotification(USAGE_NOTIFICATION_ID + usage.id.toInt())
                            getPendingIntent(usage)
                        } else null
                    }
                }

                NotificationType.COURSE -> {
                    if (notification.typeId == courseId) {
                        cancelNotification(COURSE_NOTIFICATION_ID + courseId.toInt())
                        getCourseWithParamsByIdUseCase(notification.typeId).getOrNull()
                            ?.let { course ->
                                getPendingIntent(course)
                            }
                    } else null
                }

                NotificationType.MEDICAL_CONTROL -> {
                    null // это не отменяем
                }

                NotificationType.RESCHEDULE -> {
                    null // это не отменяем
                }
            }
            pendingIntent?.let { cancelAlarmAndNotification(it, notification.id) }
        }
    }

    override suspend fun cancelAlarmByPatternUsageId(patternId: Long) {
        log("cancelAlarmByPatternUsageId: patternId=$patternId")
        getUsageDisplayByIdUseCase(patternId).getOrNull()?.let { usage ->
            cancelNotification(USAGE_NOTIFICATION_ID + usage.id.toInt())
            cancelAlarmAndNotification(getPendingIntent(usage))
            deleteNotificationInDBUseCase(
                type = NotificationType.PATTERN.ordinal,
                typeId = patternId,
            )
        }
    }

    private fun getPendingIntent(userId: Long, date: LocalDateTime): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = RESCHEDULE_NOTIFICATION_INTENT
            putExtra(Extras.USER_ID.name, userId)
        }
        return getPendingIntent(id = date.systemToEpochSecond().toInt(), intent)
    }

    private fun getPendingIntent(userId: Long, time: Long): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = CONTROL_NOTIFICATION_INTENT
            putExtra(Extras.USER_ID.name, userId)
        }
        return getPendingIntent(id = CONTROL_NOTIFICATION_ID, intent)
    }

    private fun getPendingIntent(usage: UsageDisplayDomainModel): PendingIntent {
        val useTime = usage.useTime.systemToEpochSecond()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = USAGE_NOTIFICATION_INTENT
            putExtra(Extras.USER_ID.name, usage.userId)

            putExtra(Extras.USAGE_ID.name, usage.id)
            putExtra(Extras.USAGE_TIME.name, useTime)
            putExtra(Extras.QUANTITY.name, usage.quantity)

            putExtra(Extras.REMEDY_NAME.name, usage.name)
            putExtra(Extras.REMEDY_ID.name, usage.remedyId)
            putExtra(Extras.TYPE.name, usage.type)
            putExtra(Extras.ICON.name, usage.icon)
            putExtra(Extras.COLOR.name, usage.color)
            putExtra(Extras.DOSE.name, usage.dose)
            putExtra(Extras.UNIT.name, usage.measureUnit)
        }
        return getPendingIntent(id = USAGE_NOTIFICATION_ID + usage.id.toInt(), intent)
    }

    private fun getPendingIntent(course: CourseDisplayDomainModel): PendingIntent {
        val startDate = course.endDate.plusDays(course.interval.toInt())
            .atStartOfDay()
            .systemToEpochSecond()
        val remindDate = course.remindDate?.systemToEpochSecond() ?: 0
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = COURSE_NOTIFICATION_INTENT
            putExtra(Extras.USER_ID.name, course.userId)

            putExtra(Extras.COURSE_ID.name, course.id)
            putExtra(Extras.NEW_COURSE_START_DATE.name, startDate)
            putExtra(Extras.COURSE_REMIND_TIME.name, remindDate)

            putExtra(Extras.REMEDY_NAME.name, course.name)
            putExtra(Extras.REMEDY_ID.name, course.remedyId)
            putExtra(Extras.TYPE.name, course.type)
            putExtra(Extras.ICON.name, course.icon)
            putExtra(Extras.COLOR.name, course.color)
            putExtra(Extras.DOSE.name, course.dose)
            putExtra(Extras.UNIT.name, course.measureUnit)
        }
        return getPendingIntent(id = COURSE_NOTIFICATION_ID + course.id.toInt(), intent)
    }

    private fun getPendingIntent(id: Int, intent: Intent) = PendingIntent.getBroadcast(
        context,
        id,
        intent.apply {
            data = Uri.parse("custom://$id")
        },
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
    )

    override suspend fun setFactUseTime(patternId: Long, useTime: Long) {
        log("setFactUseTime: patternId=$patternId useTime=${useTime.displayDateTime()}")
        setFactUseTimeAndInsertUsageUseCase(
            patternId = patternId,
            useTime = useTime
        )
    }

    private fun log(text: String, error: Error? = null) {
        if (error == null) {
            Log.w("VTTAG", "NotifiTag::NotificationsScheduler::$text")
        } else {
            Log.e("VTTAG", "NotifiTag::NotificationsScheduler::$text", error)
        }
    }
}
