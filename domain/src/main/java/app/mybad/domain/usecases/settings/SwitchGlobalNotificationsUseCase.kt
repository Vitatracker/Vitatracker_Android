package app.mybad.domain.usecases.settings

import app.mybad.domain.models.AuthToken
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class SwitchGlobalNotificationsUseCase @Inject constructor(
    private val notificationsScheduler: NotificationsScheduler,
) {
    suspend fun execute(enabled: Boolean) {
        if (enabled) {
            notificationsScheduler.rescheduleAlarmByUserId(AuthToken.userId)
        } else {
            notificationsScheduler.cancelAlarmByUserId(AuthToken.userId)
        }
    }
}
