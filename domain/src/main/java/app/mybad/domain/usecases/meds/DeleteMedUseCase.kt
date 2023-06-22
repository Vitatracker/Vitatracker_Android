package app.mybad.domain.usecases.meds

import app.mybad.domain.repos.CoursesNetworkRepo
import javax.inject.Inject

class DeleteMedUseCase @Inject constructor(
    private val repository: CoursesNetworkRepo
) {

    suspend operator fun invoke(medId: Long) {
        repository.deleteMed(medId)
    }

}
