package app.mybad.domain.usecases.notification

import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class AddNotificationsByPatternIdUseCase @Inject constructor(
    private val repository: NotificationsScheduler,
) {

    suspend operator fun invoke(patternId: Long) {
        repository.addAlarmByPatternUsageId(patternId)
    }
}
