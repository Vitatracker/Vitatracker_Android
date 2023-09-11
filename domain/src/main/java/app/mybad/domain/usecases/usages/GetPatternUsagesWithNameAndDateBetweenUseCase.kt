package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.repository.PatternUsageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import java.util.SortedMap
import javax.inject.Inject

class GetPatternUsagesWithNameAndDateBetweenUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(
        startTime: Long,
        endTime: Long
    ): Flow<SortedMap<String, UsageDisplayDomainModel>> =
        repository.getPatternUsagesWithNameAndDateBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime
        ).mapLatest { patterns ->
            sortedMapOf<String, UsageDisplayDomainModel>().apply {
                patterns.forEach { pattern ->
                    if (pattern.regime == 0 || startTime == pattern.startDate ||
                        ((startTime - pattern.startDate) / SECONDS_IN_DAY) % (pattern.regime + 1) == 0L
                    ) this[pattern.toUsageKey()] = pattern
                }
            }
        }

    private companion object {
        private const val SECONDS_IN_DAY = 86400
    }
}
