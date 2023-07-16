package app.mybad.domain.usecases.user

import app.mybad.domain.models.user.UserRulesDomainModel
import app.mybad.domain.repository.UserDataRepo
import javax.inject.Inject

class UpdateUserRulesUseCase @Inject constructor(
    private val userDataRepo: UserDataRepo
) {

    suspend fun execute(rulesUserDomainModel: UserRulesDomainModel) {
        userDataRepo.updateUserRules(rules = rulesUserDomainModel)
    }
}
