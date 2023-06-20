package app.mybad.domain.usecases.courses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.repos.CoursesRepo
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(
    private val repository: CoursesRepo,
) {

    suspend operator fun invoke(course: CourseDomainModel): Long =
        repository.add(course) ?: -1

}
