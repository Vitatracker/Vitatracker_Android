package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetFutureWithParamsBetweenUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    // тут для будущих курсов
    suspend operator fun invoke(startTime: Long, endTime: Long) = repository
        .getFutureWithParamsBetween(
            userId = AuthToken.userId,
            startTime = startTime,
            endTime = endTime
        )
}
