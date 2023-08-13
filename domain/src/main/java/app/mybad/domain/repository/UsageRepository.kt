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

    suspend fun getUsagesBetween(startTime: Long, endTime: Long): Flow<List<UsageDomainModel>>

    suspend fun insertUsage(usage: UsageDomainModel): Result<Unit>
    suspend fun insertUsage(usages: List<UsageDomainModel>): Result<Unit>
    suspend fun updateUsage(usage: UsageDomainModel): Result<Unit>

    suspend fun checkUseUsagesByCourseId(courseId: Long): Result<Boolean>
    suspend fun updateUsageFactTimeById(
        courseId: Long,
        usageTime: Long,
        factTime: Long
    ): Result<Unit>


    suspend fun markDeletionUsagesById(usageId: Long): Result<Unit>
    suspend fun markDeletionUsagesByCourseId(courseId: Long): Result<Unit>

    suspend fun markDeletionUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): Result<Unit>

    suspend fun markDeletionUsagesAfterByCourseId(courseId: Long, dateTime: Long): Result<Unit>

    suspend fun deleteUsagesById(usageId: Long): Result<Unit>
    suspend fun deleteUsagesByUserId(userId: Long): Result<Unit>
    suspend fun deleteUsages(usages: List<UsageDomainModel>): Result<Unit>

    suspend fun getUsagesNotUpdateByUserId(userId: Long): Result<List<UsageDomainModel>>
    suspend fun getUsagesDeletedByUserId(userId: Long): Result<List<UsageDomainModel>>
}
