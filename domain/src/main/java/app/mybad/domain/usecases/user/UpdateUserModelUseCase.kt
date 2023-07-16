package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.repository.UserDataRepo
import javax.inject.Inject

class UpdateUserModelUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(userDomainModel: UserSettingsDomainModel) {
        userDataRepo.putUser(userDomainModel = userDomainModel)
    }
}
