package app.mybad.domain.usecases.patternusage

import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetPatternUsagesByCourseIdUseCase @Inject constructor(
    private val repository: PatternUsageRepository,
) {

    suspend operator fun invoke(courseId: Long) = repository.getPatternUsagesByCourseId(courseId)
}
