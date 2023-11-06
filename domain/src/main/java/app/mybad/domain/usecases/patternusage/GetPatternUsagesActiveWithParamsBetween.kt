package app.mybad.domain.usecases.patternusage

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetPatternUsagesActiveWithParamsBetween @Inject constructor(
    private val repository: PatternUsageRepository
) {

    // тут только активные
    suspend operator fun invoke(
        startTime: Long,
        endTime: Long
    ) = repository.getPatternUsagesActiveWithParamsBetween(
        userId = AuthToken.userId,
        startTime = startTime,
        endTime = endTime
    )
}
