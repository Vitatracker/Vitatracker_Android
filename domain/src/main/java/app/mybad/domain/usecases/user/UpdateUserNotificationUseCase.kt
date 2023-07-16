package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.NotificationSettingDomainModel
import app.mybad.domain.repository.UserDataRepo
import javax.inject.Inject

class UpdateUserNotificationUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(notificationsUserDomainModel: NotificationSettingDomainModel) {
        userDataRepo.updateUserNotification(notification = notificationsUserDomainModel)
    }
}
