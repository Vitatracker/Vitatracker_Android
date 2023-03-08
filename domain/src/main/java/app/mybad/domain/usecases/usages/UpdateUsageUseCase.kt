package app.mybad.domain.usecases.usages

import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class UpdateUsageUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo
) {

    suspend fun execute(usage: UsageCommonDomainModel) {
        usagesRepo.updateSingle(usage)
    }

}