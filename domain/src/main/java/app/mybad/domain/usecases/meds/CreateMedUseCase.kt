package app.mybad.domain.usecases.meds

import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.repos.MedsRepo
import javax.inject.Inject

class CreateMedUseCase @Inject constructor(
    private val repository: MedsRepo,
) {

    suspend operator fun invoke(med: MedDomainModel): Long = repository.add(med) ?: -1

}
