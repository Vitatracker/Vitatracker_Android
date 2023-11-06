package app.mybad.domain.usecases.patternusage

import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetPatternUsageIdUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    suspend operator fun invoke(
        userId: Long,
        courseId: Long,
        timeInMinutes: Int
    ) = repository.getPatternUsageId(
        userId = userId,
        courseId = courseId,
        timeInMinutes = timeInMinutes
    ).getOrNull()
}
