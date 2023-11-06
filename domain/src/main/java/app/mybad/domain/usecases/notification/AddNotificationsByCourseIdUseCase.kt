package app.mybad.domain.usecases.notification

import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class AddNotificationsByCourseIdUseCase @Inject constructor(
    private val repository: NotificationsScheduler,
) {

    suspend operator fun invoke(courseId: Long) {
        repository.addAlarmByCourseId(courseId = courseId)
    }
}
