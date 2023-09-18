package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.PatternUsageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetPatternUsagesWithParamsBetweenUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(startTime: Long, endTime: Long) = repository
        .getPatternUsagesWithParamsBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime
        ).mapLatest { patterns ->
            patterns.associateBy { it.toUsageKey() }
        }
}
