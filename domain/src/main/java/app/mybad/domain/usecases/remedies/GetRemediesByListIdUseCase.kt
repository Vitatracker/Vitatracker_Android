package app.mybad.domain.usecases.remedies

import app.mybad.domain.repository.RemedyRepository
import javax.inject.Inject

class GetRemediesByListIdUseCase @Inject constructor(
    private val repository: RemedyRepository
) {

    suspend operator fun invoke(remedyIds: List<Long>) =
        repository.getRemediesByIds(remedyIds)
}
