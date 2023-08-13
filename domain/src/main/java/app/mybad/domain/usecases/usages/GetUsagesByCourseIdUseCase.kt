package app.mybad.domain.usecases.usages

import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class GetUsagesByCourseIdUseCase @Inject constructor(
    private val repository: UsageRepository,
) {

    suspend operator fun invoke(courseId: Long) = repository.getUsagesByCourseId(courseId)
}
