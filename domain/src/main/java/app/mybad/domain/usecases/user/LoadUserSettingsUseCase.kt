package app.mybad.domain.usecases.user

import app.mybad.domain.repos.UserDataRepo
import javax.inject.Inject

class LoadUserSettingsUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun getUserNotification() = userDataRepo.getUserNotification()

    suspend fun getUserPersonal() = userDataRepo.getUserPersonal()

    suspend fun getUserRules() = userDataRepo.getUserRules()

}