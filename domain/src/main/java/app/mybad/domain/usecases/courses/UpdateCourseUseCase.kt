package app.mybad.domain.usecases.courses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.repos.CoursesRepo
import javax.inject.Inject

class UpdateCourseUseCase @Inject constructor(
    private val coursesRepo: CoursesRepo
) {

    suspend fun execute(courseId: Long, updatedCourse: CourseDomainModel) {
        coursesRepo.updateSingle(courseId, updatedCourse)
    }
}