package app.mybad.domain.usecases.courses

import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class CreateUsagesUseCase @Inject constructor(
    private val repository: UsagesRepo,
) {

    suspend operator fun invoke(usages: List<UsageCommonDomainModel>) {
        repository.addUsages(usages)
    }
}
