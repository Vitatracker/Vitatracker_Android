package app.mybad.domain.usecases.patternusage

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetPatternUsagesWithParamsBetweenUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    // тут активные и закрытые
    suspend operator fun invoke(
        startTime: Long,
        endTime: Long,
    ) = repository
        .getPatternUsagesWithParamsBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime,
        )
}
