package app.mybad.domain.repository

import app.mybad.domain.models.PatternUsageDomainModel
import kotlinx.coroutines.flow.Flow

interface PatternUsageRepository {
    fun getPatternUsages(userId: Long): Flow<List<PatternUsageDomainModel>>
    suspend fun getPatternUsagesByUserId(userId: Long): Result<List<PatternUsageDomainModel>>
    suspend fun getPatternUsagesByCourseId(courseId: Long): Result<List<PatternUsageDomainModel>>
    suspend fun getPatternUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ): Result<List<PatternUsageDomainModel>>

    suspend fun getPatternUsagesBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ): Flow<List<PatternUsageDomainModel>>

    suspend fun insertPatternUsage(pattern: PatternUsageDomainModel): Result<Unit>
    suspend fun insertPatternUsage(patterns: List<PatternUsageDomainModel>): Result<Unit>

}
