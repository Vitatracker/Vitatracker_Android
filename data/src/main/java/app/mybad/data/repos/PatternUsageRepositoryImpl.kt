package app.mybad.data.repos

import app.mybad.data.db.dao.PatternUsageDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.utils.DAYS_IN_MONTH
import app.mybad.utils.LIMIT_START_MIN
import app.mybad.utils.SECONDS_IN_DAY
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.toEpochSecond
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.min

class PatternUsageRepositoryImpl @Inject constructor(
    private val db: PatternUsageDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : PatternUsageRepository {

    //--------------------------------------------------
    override fun getPatternUsages(userId: Long) = db.getPatternUsages(userId)
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    override suspend fun getPatternUsageId(
        userId: Long,
        courseId: Long,
        timeInMinutes: Int
    ) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsageId(
                userId = userId,
                courseId = courseId,
                timeInMinutes = timeInMinutes
            )
        }
    }

    override suspend fun getPatternUsagesByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getPatternUsagesNotUpdateByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesNotUpdateByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getPatternUsagesDeletedByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesDeletedByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getPatternUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesByCourseId(courseId = courseId).mapToDomain()
        }
    }

    override suspend fun getPatternUsageById(patternId: Long) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsageById(patternId = patternId)
                .mapToDomain()
        }
    }

    override suspend fun getUsageDisplayById(patternId: Long) = withContext(dispatcher) {
        runCatching {
            db.getUsageDisplayById(patternId = patternId)
                .mapToDomain(currentDateTimeSystem().toEpochSecond())
        }
    }

    override suspend fun getUsageDisplayByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.getUsageDisplayByCourseId(courseId = courseId)
                .mapToDomain(currentDateTimeSystem().toEpochSecond())
        }
    }

    override suspend fun getPatternUsagesWithParamsOnDate(
        userId: Long,
        startTime: Long,
        endTime: Long
    ) = withContext(dispatcher) {
        runCatching {
            db.getPatternUsagesWithParamsOnDate(
                userId = userId,
                startTime = startTime,
                endTime = endTime
            ).mapToDomain(startTime)
        }
    }

    override suspend fun getPatternUsagesActiveWithParamsBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ) = db.getPatternUsagesActiveWithParamsBetween(
        userId = userId,
        startTime = startTime,
        endTime = endTime
    )
        .map { it.mapToDomain(startTime) }
        .flowOn(dispatcher)

    override suspend fun getPatternUsagesWithParamsBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ) = db.getPatternUsagesWithParamsBetween(
        userId = userId,
        startTime = startTime,
        endTime = endTime
    )
        .map { it.mapToDomain(startTime) }
        .flowOn(dispatcher)

    override suspend fun getPatternUsagesFutureWithParamsBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ) = db.getFutureWithParamsBetween(
        userId = userId,
        // захватим интервал от минимального старта курса
        startTime = min(0L, startTime - LIMIT_START_MIN * DAYS_IN_MONTH * SECONDS_IN_DAY),
        endTime = endTime,
    )
        .map { usages ->
            usages.filter { usage ->
                usage.isInfinite || (endTime >= usage.startDate && startTime <= usage.endDate)
            }.mapToDomain(startTime)
        }
        .flowOn(dispatcher)

    override suspend fun insertPatternUsage(pattern: PatternUsageDomainModel) =
        withContext(dispatcher) {
            runCatching {
                db.insertPatternUsage(pattern.mapToData())
            }
        }

    override suspend fun insertPatternUsage(patterns: List<PatternUsageDomainModel>) =
        withContext(dispatcher) {
            runCatching {
                db.insertPatternUsages(patterns.mapToData())
            }
        }

    override suspend fun finishedPatternUsageByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.finishedPatternUsageByCourseId(courseId)
        }
    }

    override suspend fun markDeletionPatternUsage(id: Long) = withContext(dispatcher) {
        runCatching {
            db.markDeletionPatternUsage(id)
        }
    }

    override suspend fun markDeletionPatternUsageByCourseId(courseId: Long) =
        withContext(dispatcher) {
            runCatching {
                db.markDeletionPatternUsagesByCourseId(courseId)
            }
        }

    override suspend fun deletePatternUsagesByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.deletePatternUsagesByUserId(userId)
        }
    }

    override suspend fun deletePatternUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.deletePatternUsagesByCourseId(courseId = courseId)
        }
    }

    override suspend fun deletePatternUsage(id: Long) = withContext(dispatcher) {
        runCatching {
            db.deletePatternUsage(id)
        }
    }

    override suspend fun deletePatternUsages(patterns: List<PatternUsageDomainModel>) =
        withContext(dispatcher) {
            runCatching {
                db.deletePatternUsages(patterns.mapToData())
            }
        }

}
