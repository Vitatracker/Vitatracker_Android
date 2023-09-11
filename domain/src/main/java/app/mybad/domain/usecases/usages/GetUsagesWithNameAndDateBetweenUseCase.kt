package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.repository.UsageRepository
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetUsagesWithNameAndDateBetweenUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    // тут должны все попасть на определенную дату, т.е. useTime in startTime..endTime
    suspend operator fun invoke(startTime: Long, endTime: Long) = repository
        .getUsagesWithNameAndDateBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime
        ).mapLatest { usages ->
            val r = sortedMapOf<String, UsageDisplayDomainModel>().apply {
                usages.forEach { this[it.toUsageKey()] = it }
            }
            Log.d("VTTAG", "GetUsagesWithNameAndDateBetweenUseCase: usages=${r.size}")
            r
        }
}
