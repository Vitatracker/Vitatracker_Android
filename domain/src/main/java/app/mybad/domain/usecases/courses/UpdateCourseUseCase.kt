package app.mybad.domain.usecases.courses

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.repository.CourseRepository
import javax.inject.Inject

class UpdateCourseUseCase @Inject constructor(
    private val coursesRepo: CourseRepository
) {

    suspend operator fun invoke(updatedCourse: CourseDomainModel) {
        coursesRepo.updateCourse(updatedCourse)
    }
}
