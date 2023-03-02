package app.mybad.domain.scheduler

import app.mybad.domain.models.usages.UsageCommonDomainModel

interface NotificationsScheduler {
    fun add(usages: List<UsageCommonDomainModel>)
    fun cancel(usages: List<UsageCommonDomainModel>)
    fun cancelByMedId(medId: Long)
}