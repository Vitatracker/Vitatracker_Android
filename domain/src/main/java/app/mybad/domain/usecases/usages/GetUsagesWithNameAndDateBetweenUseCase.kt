package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UsageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetUsagesWithNameAndDateBetweenUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    // тут должны все попасть на определенную дату, т.е. useTime in startTime..endTime
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(startTime: Long, endTime: Long) = repository
        .getUsagesWithNameAndDateBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime
        ).mapLatest { usages ->
            usages.associateBy { it.toUsageKey() }
        }
}
