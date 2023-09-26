package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class GetUsagesWithParamsBetweenUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    // тут должны все попасть на определенную дату, т.е. useTime in startTime..endTime
    suspend operator fun invoke(startTime: Long, endTime: Long) = repository
        .getUsagesWithParamsBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime
        )
}
