package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import javax.inject.Inject

class GetCourseByIdUseCase @Inject constructor(
    private val repository: CourseRepository,
) {

    suspend operator fun invoke(courseId: Long) = repository.getCourseById(courseId)
}
