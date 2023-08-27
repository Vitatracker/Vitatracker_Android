package app.mybad.data.repos

import app.mybad.data.db.dao.PatternUsageDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.repository.PatternUsageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PatternUsageRepositoryImpl @Inject constructor(
    private val db: PatternUsageDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : PatternUsageRepository {

//--------------------------------------------------
    override fun getPatternUsages(userId: Long)= db.getPatternUsages(userId)
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    override suspend fun getPatternUsagesByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getPatternUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesByCourseId(courseId).mapToDomain()
        }
    }

    override suspend fun getPatternUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesBetweenByCourseId(
                courseId = courseId,
                startTime = startTime,
                endTime = endTime
            ).mapToDomain()
        }
    }

    override suspend fun getPatternUsagesBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ) = db.getPatternUsagesBetween(
        userId = userId,
        startTime = startTime,
        endTime = endTime
    )
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    override suspend fun insertPatternUsage(pattern: PatternUsageDomainModel) = withContext(dispatcher) {
        runCatching {
            db.insertPatternUsage(pattern.mapToData())
        }
    }

    override suspend fun insertPatternUsage(patterns: List<PatternUsageDomainModel>) = withContext(dispatcher) {
        runCatching {
            db.insertPatternUsages(patterns.mapToData())
        }
    }
}
