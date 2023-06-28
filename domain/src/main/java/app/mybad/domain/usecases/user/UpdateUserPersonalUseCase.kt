package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.repos.UserDataRepo
import javax.inject.Inject

class UpdateUserPersonalUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(personalDomainModel: PersonalDomainModel) {
        userDataRepo.updateUserPersonal(personalDomainModel)
    }
}
