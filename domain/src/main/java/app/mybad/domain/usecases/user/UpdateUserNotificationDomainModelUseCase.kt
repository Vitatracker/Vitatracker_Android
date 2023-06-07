package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.repos.UserDataRepo
import javax.inject.Inject

class UpdateUserNotificationDomainModelUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(notificationsUserDomainModel: NotificationsUserDomainModel) {
        userDataRepo.updateUserNotification(notification = notificationsUserDomainModel)
    }
}
