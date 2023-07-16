package app.mybad.domain.usecases.user

import app.mybad.domain.repository.UserDataRepo
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun getUserModel() = userDataRepo.getUser()

    suspend fun getUserNotification() = userDataRepo.getUserNotification()

    suspend fun getUserPersonal() = userDataRepo.getUserPersonal()

    suspend fun getUserRules() = userDataRepo.getUserRules()
}
