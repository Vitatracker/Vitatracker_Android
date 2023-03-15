package app.mybad.domain.usecases.settings

import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class SwitchGlobalNotificationsUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo,
    private val notificationsScheduler: NotificationsScheduler
) {
    suspend fun execute(enabled: Boolean) {
        val usages = usagesRepo.getCommonAll()
        if(enabled) notificationsScheduler.add(usages)
        else notificationsScheduler.cancel(usages)
    }
}