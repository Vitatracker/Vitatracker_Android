package app.mybad.domain.usecases.courses

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.repository.CourseRepository
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(
    private val repository: CourseRepository,
) {

    suspend operator fun invoke(course: CourseDomainModel) = repository.insertCourse(course)

}
