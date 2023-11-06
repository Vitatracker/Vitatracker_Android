package app.mybad.domain.usecases.notification

import app.mybad.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationInDBUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {

    suspend operator fun invoke(userId: Long) = repository.getNotificationByUserId(userId)

}
