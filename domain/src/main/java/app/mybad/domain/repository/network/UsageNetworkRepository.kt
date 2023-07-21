package app.mybad.domain.repository.network

import app.mybad.domain.models.UsageDomainModel

interface UsageNetworkRepository {

    suspend fun getUsagesByCourseId(courseId: Long): Result<List<UsageDomainModel>>
    suspend fun updateUsage(usage: UsageDomainModel): UsageDomainModel
    suspend fun deleteUsage(usageId: Long): Result<Boolean>
}
