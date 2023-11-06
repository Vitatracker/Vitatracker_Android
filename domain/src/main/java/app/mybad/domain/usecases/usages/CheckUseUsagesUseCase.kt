package app.mybad.domain.usecases.usages

import app.mybad.domain.repository.UsageRepository
import app.mybad.utils.systemToEpochSecond
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class CheckUseUsagesUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    suspend operator fun invoke(
        courseId: Long,
        useTime: LocalDateTime,
    ) = repository.checkUseUsages(courseId, useTime.systemToEpochSecond()).getOrNull() ?: false
}
