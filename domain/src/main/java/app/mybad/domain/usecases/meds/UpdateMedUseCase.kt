package app.mybad.domain.usecases.meds

import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.repos.MedsRepo
import javax.inject.Inject

class UpdateMedUseCase @Inject constructor(
    private val medsRepo: MedsRepo
) {

    suspend operator fun invoke(med: MedDomainModel) {
        medsRepo.updateSingle(med.id, med)
    }
}
