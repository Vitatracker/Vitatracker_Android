package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.repos.UserDataRepo
import javax.inject.Inject

class UpdateUserRulesUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(rulesUserDomainModel: RulesUserDomainModel) {
        userDataRepo.updateUserRules(rules = rulesUserDomainModel)
    }
}
