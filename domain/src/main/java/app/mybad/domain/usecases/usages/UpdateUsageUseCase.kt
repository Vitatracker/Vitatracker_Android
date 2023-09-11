package app.mybad.domain.usecases.usages

import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class UpdateUsageUseCase @Inject constructor(
    private val usageRepository: UsageRepository,
) {

    suspend operator fun invoke(usage: UsageDomainModel) = usageRepository.updateUsage(usage)
}
