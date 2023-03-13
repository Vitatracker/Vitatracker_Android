package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.repos.UserDataRepo
import javax.inject.Inject

class UpdateUserPersonalDomainModelUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(personalDomainModel: PersonalDomainModel) {
        userDataRepo.updateUserPersonal(personalDomainModel)
    }

}