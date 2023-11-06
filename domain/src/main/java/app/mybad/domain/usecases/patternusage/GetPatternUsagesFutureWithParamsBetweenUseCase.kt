package app.mybad.domain.usecases.patternusage

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetPatternUsagesFutureWithParamsBetweenUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    // тут для будущих курсов
    suspend operator fun invoke(startTime: Long, endTime: Long) = repository
        .getPatternUsagesFutureWithParamsBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime
        )
}
