package app.mybad.domain.usecases.notification

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationInDBByIdUseCase @Inject constructor(
    private val repository: NotificationRepository,
) {

    suspend operator fun invoke(id: Long) = repository.deleteNotificationById(
        userId = AuthToken.userId,
        id = id
    )

}
