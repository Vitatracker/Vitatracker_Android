package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import javax.inject.Inject

class GetCoursesWithRemindBetweenDateUseCase @Inject constructor(
    private val repository: CourseRepository,
) {

    suspend operator fun invoke(
        userId: Long,
        startTime: Long,
        endTime: Long,
    ) = repository.getCoursesWithRemindDateBetween(
        userId = userId,
        startTime = startTime,
        endTime = endTime
    )
}
