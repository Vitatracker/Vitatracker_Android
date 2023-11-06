package app.mybad.domain.usecases.notification

import app.mybad.domain.models.AuthToken
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class CancelNotificationsByCourseIdUseCase @Inject constructor(
    private val repository: NotificationsScheduler,
) {

    suspend operator fun invoke(courseId: Long) {
        repository.cancelAlarmByCourseId(userId = AuthToken.userId, courseId = courseId)
    }
}
