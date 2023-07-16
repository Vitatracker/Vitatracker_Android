package app.mybad.domain.usecases.courses

import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val repository: CourseRepository,
    private val networkRepository: CourseNetworkRepository,
) {

    suspend operator fun invoke(userId: Long = AuthToken.userId): Result<List<CourseDomainModel>> {
        //TODO("реализовать загрузку данных из бека")
//        networkRepository.getAll()
        return repository.getCoursesByUserId(userId)
    }

}
