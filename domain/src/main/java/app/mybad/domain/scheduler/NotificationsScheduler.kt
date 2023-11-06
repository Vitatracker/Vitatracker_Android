package app.mybad.domain.scheduler

import kotlinx.datetime.LocalDateTime

interface NotificationsScheduler {
    suspend fun rescheduleAlarm()
    suspend fun rescheduleAlarmByUserId(userId: Long, onComplete: () -> Unit = {})

    suspend fun setFactUseTime(patternId: Long, useTime: Long)

    //    suspend fun addAlarmForNextDay(userId: Long)
    suspend fun addAlarmTest()
    suspend fun addAlarmControl(userId: Long)

    //    suspend fun addAlarmUsages(userId: Long, date: LocalDateTime = currentDateTimeSystem())
//    suspend fun addAlarmCourses(userId: Long, date: LocalDateTime = currentDateTimeSystem())
    suspend fun addAlarmByCourseId(courseId: Long)
    suspend fun addAlarmByPatternUsageId(
        patternId: Long,
        notificationTime: LocalDateTime? = null,
    )

    suspend fun cancelAlarm(userId: Long)
    suspend fun cancelAlarmControl(userId: Long)
    suspend fun cancelAlarmByCourseId(userId: Long, courseId: Long)
    suspend fun cancelAlarmByPatternUsageId(patternId: Long)

    companion object {
        const val USAGE_NOTIFICATION_INTENT = "android.intent.action.NOTIFICATION"
        const val COURSE_NOTIFICATION_INTENT = "android.intent.action.COURSE_NOTIFICATION"
        const val CONTROL_NOTIFICATION_INTENT = "android.intent.action.CONTROL_NOTIFICATION"
        const val RESCHEDULE_NOTIFICATION_INTENT = "android.intent.action.RESCHEDULE_NOTIFICATION"

        const val TAKE_INTENT = "android.intent.action.TAKE_INTENT"
        const val DELAY_INTENT = "android.intent.action.DELAY_INTENT"
        const val FORCE_CLOSE = "android.intent.action.FORCE_CLOSE"

        const val USAGE_NOTIFICATION_ID = 110000000
        const val COURSE_NOTIFICATION_ID = 120000000
        const val CONTROL_NOTIFICATION_ID = 130000001
    }
}
