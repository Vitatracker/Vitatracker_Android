package app.mybad.domain.repository

import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.models.UsageDisplayDomainModel
import kotlinx.coroutines.flow.Flow

interface PatternUsageRepository {
    fun getPatternUsages(userId: Long): Flow<List<PatternUsageDomainModel>>
    suspend fun getPatternUsagesByUserId(userId: Long): Result<List<PatternUsageDomainModel>>
    suspend fun getPatternUsagesNotUpdateByUserId(userId: Long): Result<List<PatternUsageDomainModel>>
    suspend fun getPatternUsagesDeletedByUserId(userId: Long): Result<List<PatternUsageDomainModel>>
    suspend fun getPatternUsagesByCourseId(courseId: Long): Result<List<PatternUsageDomainModel>>

    //    suspend fun getPatternUsagesBetweenByCourseId(
//        courseId: Long,
//        startTime: Long,
//        endTime: Long
//    ): Result<List<PatternUsageDomainModel>>
//
//    suspend fun getPatternUsagesBetween(
//        userId: Long,
//        startTime: Long,
//        endTime: Long
//    ): Flow<List<PatternUsageDomainModel>>
//
    suspend fun getPatternUsagesWithNameAndDateBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<UsageDisplayDomainModel>>

    suspend fun getPatternUsagesWithParamsBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<UsageDisplayDomainModel>>

    suspend fun getFutureWithParamsBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<UsageDisplayDomainModel>>

    suspend fun insertPatternUsage(pattern: PatternUsageDomainModel): Result<Unit>
    suspend fun insertPatternUsage(patterns: List<PatternUsageDomainModel>): Result<Unit>
    suspend fun finishedPatternUsageByCourseId(courseId: Long): Result<Unit>
    suspend fun markDeletionPatternUsage(id: Long): Result<Unit>
    suspend fun markDeletionPatternUsageByCourseId(courseId: Long): Result<Unit>

    suspend fun deletePatternUsagesByUserId(userId: Long): Result<Unit>
    suspend fun deletePatternUsagesByCourseId(courseId: Long): Result<Unit>
    suspend fun deletePatternUsage(id: Long): Result<Unit>
    suspend fun deletePatternUsages(patterns: List<PatternUsageDomainModel>): Result<Unit>
}
