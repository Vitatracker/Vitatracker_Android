package app.mybad.domain.usecases.courses

import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class CreateUsageUseCase @Inject constructor(
    private val repository: UsageRepository,
) {

    suspend operator fun invoke(usages: List<UsageDomainModel>) {
        repository.insertUsages(usages)
    }
}
