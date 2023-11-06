package app.mybad.domain.usecases.notification

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationInDBUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {

    suspend operator fun invoke(type: Int, typeId: Long) = repository.deleteNotification(
        userId = AuthToken.userId,
        type = type,
        typeId = typeId,
    )

}
