package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class GetUsagesBetweenUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    suspend operator fun invoke(startTime: Long, endTime: Long) = repository
        .getUsagesBetween(userId = AuthToken.userId, startTime = startTime, endTime = endTime)
}
