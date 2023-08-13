package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.domain.repository.UserDataRepo
import javax.inject.Inject

class UpdateUserPersonalUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend operator fun invoke(personalDomainModel: UserPersonalDomainModel) {
        userDataRepo.updateUserPersonal(personalDomainModel)
    }
}
