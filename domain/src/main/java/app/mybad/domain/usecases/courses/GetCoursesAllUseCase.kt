package app.mybad.domain.usecases.courses

import app.mybad.domain.repos.CoursesNetworkRepo
import javax.inject.Inject

class GetCoursesAllUseCase @Inject constructor(
    private val repository: CoursesNetworkRepo
) {

    suspend operator fun invoke() {
        repository.getAll()
    }

}
