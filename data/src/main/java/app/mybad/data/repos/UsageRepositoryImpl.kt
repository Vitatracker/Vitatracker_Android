package app.mybad.data.repos

import app.mybad.data.db.dao.UsageDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

class UsageRepositoryImpl @Inject constructor(
    private val db: UsageDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UsageRepository {

    override fun getUsages(userId: Long) = db.getUsages(userId)
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    override suspend fun getUsagesByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getUsagesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getUsagesByCourseId(courseId).mapToDomain()
        }
    }

    override suspend fun getUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ) = withContext(dispatcher) {
        Result.runCatching {
            db.getUsagesBetweenById(courseId, startTime, endTime).mapToDomain()
        }
    }

    //TODO("не понятно что тут происходит")
    override suspend fun updateUsageFactTimeById(courseId: Long, usageTime: Long, factTime: Long) =
        withContext(dispatcher) {
            Result.runCatching {
                db.getUsagesByCourseId(courseId)
                    .lastOrNull { usage ->
                        usage.courseId == courseId && usage.useTime == usageTime
                    }?.copy(factUseTime = factTime)?.let { usage ->
                        // TODO("добавлять в другую базу")
                        db.insertUsage(usage)
                    }
            }
        }

    override suspend fun insertUsage(usage: UsageDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            db.insertUsage(usage.mapToData())
        }
    }

    override suspend fun insertUsage(usages: List<UsageDomainModel>) = withContext(dispatcher) {
        Result.runCatching {
            db.insertUsages(usages.mapToData())
        }
    }

    override suspend fun updateUsage(usage: UsageDomainModel) = insertUsage(usage)

    override suspend fun delete(courseId: Long, dateTime: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.delete(courseId, dateTime)
        }
    }

    override suspend fun deleteUsagesById(usageId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.deleteUsagesById(usageId)
        }
    }

    override suspend fun deleteUsages(usages: List<UsageDomainModel>) = withContext(dispatcher) {
        Result.runCatching {
            db.deleteUsages(usages.mapToData())
        }
    }

    override suspend fun deleteUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.deleteUsagesByCourseId(courseId)
        }
    }

    override suspend fun deleteUsagesBetweenById(courseId: Long, startTime: Long, endTime: Long) =
        withContext(dispatcher) {
            Result.runCatching {
                db.deleteUsagesBetweenById(courseId, startTime, endTime)
            }
        }

    override suspend fun deleteUsagesAfter(courseId: Long, afterTime: Long) =
        withContext(dispatcher) {
            Result.runCatching {
                db.deleteUsagesAfter(courseId, afterTime)
            }
        }

    override suspend fun getUsagesBetween(
        startTime: Long,
        endTime: Long
    ) = withContext(dispatcher) {
        Result.runCatching {
            db.getUsagesBetween(startTime = startTime, endTime = endTime).mapToDomain()
        }
    }

    override suspend fun getUsagesNotUpdateByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getUsagesNotUpdateByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getUsagesDeletedByUserId(userId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getUsagesDeletedByUserId(userId).mapToDomain()
        }
    }
}
