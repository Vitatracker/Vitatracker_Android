package app.mybad.domain.repository.network

import app.mybad.domain.models.UsageDomainModel

interface UsageNetworkRepository {

    suspend fun getUsages(): Result<List<UsageDomainModel>>
    suspend fun getUsagesByCourseId(
        courseId: Long,
        remedyIdLoc: Long = 0,
        courseIdLoc: Long = 0,
    ): Result<List<UsageDomainModel>>

    suspend fun updateUsage(usage: UsageDomainModel): Result<UsageDomainModel>
    suspend fun deleteUsage(usageId: Long): Result<Boolean>
}
