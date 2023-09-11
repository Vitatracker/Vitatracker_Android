package app.mybad.domain.usecases.courses

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val repository: CourseRepository,
) {

    operator fun invoke(userId: Long = AuthToken.userId) = repository.getCoursesWithParams(userId)
}
