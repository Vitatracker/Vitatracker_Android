package app.mybad.domain.usecases.user

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.NotificationRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.UserRepository
import app.mybad.domain.repository.network.SettingsNetworkRepository
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class DeleteUserAccountUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val patternUsageRepository: PatternUsageRepository,

    private val notificationsScheduler: NotificationsScheduler,
    private val notificationRepository: NotificationRepository,

    private val userRepository: UserRepository,
    private val userNetworkRepository: SettingsNetworkRepository,
) {

    suspend operator fun invoke(): Boolean {
        //TODO("пересмотреть репозитории и базы и доделать удаление пользователя")
        return if (AuthToken.userId > 0) {
            val userId = AuthToken.userId
            val user = userRepository.getUserById(userId)
            // вначале удаляем с сервера, если ошибка, то локально не удаляем
            userNetworkRepository.deleteUser(user.idn).onSuccess {
                deleteLocal(userId)
            }.onFailure {
            }
            deleteLocal(userId)
        } else false
    }

    private suspend fun deleteLocal(userId:Long) = try {
        usageRepository.deleteUsagesByUserId(userId)
        patternUsageRepository.deletePatternUsagesByUserId(userId)
        courseRepository.deleteCoursesByUserId(userId)
        remedyRepository.deleteRemediesByUserId(userId)

        notificationsScheduler.cancelAlarm(userId)
        notificationRepository.deleteNotificationByUserId(userId)

        userRepository.deleteUserById(userId)
        AuthToken.clear()
        true
    } catch (e:Error){
        false
    }
}
