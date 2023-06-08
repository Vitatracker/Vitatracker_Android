package app.mybad.domain.scheduler

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel

interface NotificationsScheduler {
    suspend fun add(usages: List<UsageCommonDomainModel>)
    suspend fun add(course: CourseDomainModel)
    suspend fun rescheduleAll(userId: Long, onComplete: () -> Unit = {})
    suspend fun cancel(usages: List<UsageCommonDomainModel>)
    suspend fun cancel(course: CourseDomainModel)
    suspend fun cancelAll(userId: Long)
    suspend fun cancelByMedId(medId: Long, onComplete: suspend () -> Unit)
}
