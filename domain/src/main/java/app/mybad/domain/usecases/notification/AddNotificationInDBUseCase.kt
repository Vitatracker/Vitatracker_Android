package app.mybad.domain.usecases.notification

import app.mybad.domain.models.NotificationDomainModel
import app.mybad.domain.repository.NotificationRepository
import javax.inject.Inject

class AddNotificationInDBUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {

    suspend operator fun invoke(notification: NotificationDomainModel) =
        repository.addNotification(notification)

}
