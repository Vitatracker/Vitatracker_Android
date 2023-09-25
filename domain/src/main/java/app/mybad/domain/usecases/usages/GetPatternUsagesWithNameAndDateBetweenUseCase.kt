package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetPatternUsagesWithNameAndDateBetweenUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    suspend operator fun invoke(
        startTime: Long,
        endTime: Long
    ) = repository.getPatternUsagesWithNameAndDateBetween(
        userId = AuthToken.userId,
        startTime = startTime,
        endTime = endTime
    )
}
