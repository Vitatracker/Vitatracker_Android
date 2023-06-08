package app.mybad.domain.usecases.settings

import app.mybad.domain.repos.DataStoreRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SwitchGlobalNotificationsUseCase @Inject constructor(
    private val notificationsScheduler: NotificationsScheduler,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend fun execute(enabled: Boolean) {
        val userId = dataStoreRepo.getUserId().first().toLong()
        if (enabled) {
            notificationsScheduler.rescheduleAll(userId)
        } else {
            notificationsScheduler.cancelAll(userId)
        }
    }
}
