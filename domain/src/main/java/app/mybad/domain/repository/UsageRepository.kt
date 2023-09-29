package app.mybad.domain.repository

import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.models.UsageDomainModel
import kotlinx.coroutines.flow.Flow

interface UsageRepository {
    fun getUsages(userId: Long): Flow<List<UsageDomainModel>>
    suspend fun getUsagesByUserId(userId: Long): Result<List<UsageDomainModel>>
    suspend fun getUsagesByCourseId(courseId: Long): Result<List<UsageDomainModel>>
    suspend fun getUsageById(usageId: Long): Result<UsageDomainModel?>
    suspend fun getUsageByParams(
        userId: Long,
        courseId: Long,
        useTime: Long,
    ): Result<UsageDomainModel?>

    suspend fun getUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): Result<List<UsageDomainModel>>

    suspend fun getUsagesBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Result<List<UsageDomainModel>>

    suspend fun getUsagesWithNameAndDateBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<UsageDisplayDomainModel>>

    suspend fun getUsagesWithParamsBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<UsageDisplayDomainModel>>

    suspend fun insertUsage(usage: UsageDomainModel): Result<Long>
    suspend fun insertUsage(usages: List<UsageDomainModel>): Result<Unit>
    suspend fun updateUsage(usage: UsageDomainModel): Result<Long>
    suspend fun setFactUseTimeUsage(usageId: Long, factUseTime: Long?): Result<Long>

    suspend fun checkUseUsagesByCourseId(courseId: Long): Result<Boolean>

    suspend fun markDeletionUsagesById(usageId: Long): Result<Unit>
    suspend fun markDeletionUsagesByCourseId(courseId: Long): Result<Unit>

    suspend fun markDeletionUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): Result<Unit>

    suspend fun markDeletionUsagesAfterByCourseId(courseId: Long): Result<Unit>

    suspend fun deleteUsagesById(usageId: Long): Result<Unit>
    suspend fun deleteUsagesByUserId(userId: Long): Result<Unit>
    suspend fun deleteUsagesByCourseId(courseId: Long): Result<Unit>
    suspend fun deleteUsagesByCourseIdn(courseIdn: Long): Result<Unit>
    suspend fun deleteUsages(usages: List<UsageDomainModel>): Result<Unit>

    suspend fun getUsagesNotUpdateByUserId(userId: Long): Result<List<UsageDomainModel>>
    suspend fun getUsagesDeletedByUserId(userId: Long): Result<List<UsageDomainModel>>
}
