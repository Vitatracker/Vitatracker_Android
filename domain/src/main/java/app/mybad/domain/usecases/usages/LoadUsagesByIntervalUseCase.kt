package app.mybad.domain.usecases.usages

import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class LoadUsagesByIntervalUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo
) {

    suspend fun execute(startTime: Long, endTime: Long) =
        usagesRepo.getUsagesByInterval(startTime = startTime, endTime = endTime)
}
