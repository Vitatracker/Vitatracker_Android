package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import javax.inject.Inject

class CountActiveCourseUseCase @Inject constructor(
    private val repository: CourseRepository,
) {

    suspend operator fun invoke(userId: Long) = repository.countActiveCourses(userId)
}
