package app.mybad.domain.usecases.usages

import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class GetUsagesByMedIdUseCase @Inject constructor(
    private val repository: UsagesRepo,
) {

    suspend operator fun invoke(medId: Long) = repository.getUsagesByMedId(medId)
}
