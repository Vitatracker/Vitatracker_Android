package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.repos.UserDataRepo
import javax.inject.Inject

class UpdateUserDataModelUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(userDomainModel: UserDomainModel) {
        userDataRepo.updateUserModel(userModel = userDomainModel)
    }

}