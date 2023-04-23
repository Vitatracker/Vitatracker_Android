package app.mybad.domain.usecases.usages

import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class UpdateUsageFactTimeUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo
) {

    suspend fun execute(medId: Long, usageTime: Long, factTime: Long) {
        usagesRepo.setUsageTime(
            medId = medId,
            usageTime = usageTime,
            factTime = factTime
        )
    }

}