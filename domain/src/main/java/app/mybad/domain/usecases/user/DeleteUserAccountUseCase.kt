package app.mybad.domain.usecases.user

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.UserRepository
import app.mybad.domain.repository.network.SettingsNetworkRepository
import javax.inject.Inject

class DeleteUserAccountUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val patternUsageRepository: PatternUsageRepository,

    private val userRepository: UserRepository,
    private val userNetworkRepository: SettingsNetworkRepository,
) {

    suspend operator fun invoke(): Boolean {
        //TODO("пересмотреть репозитории и базы и доделать удаление пользователя")
        if (AuthToken.userId > 0) {
            val userId = AuthToken.userId
            val user = userRepository.getUserById(userId)
            // вначале удаляем с сервера, если ошибка, то локально не удаляем
            userNetworkRepository.deleteUser(user.idn).onSuccess {
                if (it) {
                    usageRepository.deleteUsagesByUserId(userId)
                    patternUsageRepository.deletePatternUsagesByUserId(userId)
                    courseRepository.deleteCoursesByUserId(userId)
                    remedyRepository.deleteRemediesByUserId(userId)

                    userRepository.deleteUserById(userId)
                    AuthToken.clear()
                    return true
                }
            }.onFailure {
            }
        }
        return false
    }

}
