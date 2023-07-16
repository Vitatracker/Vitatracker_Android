package app.mybad.domain.usecases.remedies

import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.repository.RemedyRepository
import javax.inject.Inject

class UpdateRemedyUseCase @Inject constructor(
    private val medsRepo: RemedyRepository
) {

    suspend operator fun invoke(remedy: RemedyDomainModel) {
        medsRepo.updateRemedy(remedy)
    }
}
