package app.mybad.domain.usecases.remedies

import app.mybad.domain.repository.network.CourseNetworkRepository
import javax.inject.Inject

class DeleteRemedyUseCase @Inject constructor(
    private val repository: CourseNetworkRepository
) {

    suspend operator fun invoke(medId: Long) {
        TODO("написать код")
//        repository.deleteMed(medId)
    }

}
