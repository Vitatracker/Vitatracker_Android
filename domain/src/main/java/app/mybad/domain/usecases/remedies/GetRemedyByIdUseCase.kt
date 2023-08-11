package app.mybad.domain.usecases.remedies

import app.mybad.domain.repository.RemedyRepository
import javax.inject.Inject

class GetRemedyByIdUseCase @Inject constructor(
    private val repository: RemedyRepository
) {

    suspend operator fun invoke(remedyId: Long) = repository.getRemedyById(remedyId)
}
