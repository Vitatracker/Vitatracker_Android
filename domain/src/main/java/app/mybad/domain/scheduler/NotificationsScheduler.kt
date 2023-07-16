package app.mybad.domain.scheduler

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.UsageDomainModel

interface NotificationsScheduler {
    suspend fun addAlarm(usages: List<UsageDomainModel>)
    suspend fun addAlarm(course: CourseDomainModel)
    suspend fun rescheduleAlarmByUserId(userId: Long, onComplete: () -> Unit = {})
    suspend fun cancelAlarm(usages: List<UsageDomainModel>)
    suspend fun cancelAlarm(course: CourseDomainModel)
    suspend fun cancelAlarmByUserId(userId: Long)
    suspend fun cancelAlarmByCourseId(courseId: Long, onComplete: suspend () -> Unit)
}
