package app.mybad.domain.repository

import app.mybad.domain.models.UsageDomainModel
import kotlinx.coroutines.flow.Flow

interface UsageRepository {
    fun getUsages(userId: Long): Flow<List<UsageDomainModel>>
    suspend fun getUsagesByUserId(userId: Long): Result<List<UsageDomainModel>>
    suspend fun getUsagesByCourseId(courseId: Long): Result<List<UsageDomainModel>>
    suspend fun getUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): Result<List<UsageDomainModel>>

    suspend fun getUsagesBetween(startTime: Long, endTime: Long): Result<List<UsageDomainModel>>
    suspend fun insertUsage(usage: UsageDomainModel)
    suspend fun insertUsages(usages: List<UsageDomainModel>)
    suspend fun updateUsageFactTimeById(courseId: Long, usageTime: Long, factTime: Long)
    suspend fun deleteUsagesById(courseId: Long)
    suspend fun deleteUsagesBetweenById(courseId: Long, startTime: Long, endTime: Long)
    suspend fun deleteUsagesAfter(courseId: Long, afterTime: Long)
    suspend fun getUsagesNotUpdateByUserId(userId: Long): Result<List<UsageDomainModel>>
}
