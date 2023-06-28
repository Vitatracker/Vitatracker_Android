package app.mybad.domain.usecases.courses

import app.mybad.domain.repos.CoursesRepo
import javax.inject.Inject

class GetCourseSingleUseCase @Inject constructor(
    private val repository: CoursesRepo,
) {

    suspend operator fun invoke(courseId: Long) = repository.getSingle(courseId)
}
