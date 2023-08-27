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
        runCatching {
            db.getUsagesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.getUsagesByCourseId(courseId).mapToDomain()
        }
    }

    override suspend fun getUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ) = withContext(dispatcher) {
        runCatching {
            db.getUsagesBetweenByCourseId(
                courseId = courseId,
                startTime = startTime,
                endTime = endTime
            ).mapToDomain()
        }
    }

    override suspend fun getUsagesBetween(
        userId: Long,
        startTime: Long,
        endTime: Long
    ) = db.getUsagesBetween(
        userId = userId,
        startTime = startTime,
        endTime = endTime
    )
        .map { it.mapToDomain() }
        .flowOn(dispatcher)

    //--------------------------------------------------
    override suspend fun checkUseUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.checkUseUsagesByCourseId(courseId) != null
        }
    }

    // установить время использования таблетки
    override suspend fun updateUsageFactTimeById(courseId: Long, usageTime: Long, factTime: Long) =
        withContext(dispatcher) {
            runCatching {
                db.setFactUseTime(
                    courseId = courseId,
                    usageTime = usageTime,
                    factTime = factTime,
                )
            }
        }

    //--------------------------------------------------
    override suspend fun insertUsage(usage: UsageDomainModel) = withContext(dispatcher) {
        runCatching {
            db.insertUsage(usage.mapToData())
        }
    }

    override suspend fun insertUsage(usages: List<UsageDomainModel>) = withContext(dispatcher) {
        runCatching {
            db.insertUsages(usages.mapToData())
        }
    }

    override suspend fun updateUsage(usage: UsageDomainModel) = insertUsage(usage)

    //--------------------------------------------------
    override suspend fun markDeletionUsagesById(usageId: Long) = withContext(dispatcher) {
        runCatching {
            db.markDeletionUsagesById(usageId)
        }
    }

    override suspend fun markDeletionUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        runCatching {
            db.markDeletionUsagesByCourseId(courseId)
        }
    }

    override suspend fun markDeletionUsagesAfterByCourseId(courseId: Long, dateTime: Long) =
        withContext(dispatcher) {
            runCatching {
                db.markDeletionUsagesAfterByCourseId(courseId = courseId, dateTime = dateTime)
            }
        }

    override suspend fun markDeletionUsagesBetweenByCourseId(
        courseId: Long,
        startTime: Long,
        endTime: Long
    ) =
        withContext(dispatcher) {
            runCatching {
                db.markDeletionUsagesBetweenByCourseId(
                    courseId = courseId,
                    startTime = startTime,
                    endTime = endTime
                )
            }
        }

    //--------------------------------------------------
    override suspend fun deleteUsagesById(usageId: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteUsagesById(usageId)
        }
    }

    override suspend fun deleteUsagesByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteUsagesByUserId(userId)
        }
    }

    override suspend fun deleteUsages(usages: List<UsageDomainModel>) = withContext(dispatcher) {
        runCatching {
            db.deleteUsages(usages.mapToData())
        }
    }

    //--------------------------------------------------
    override suspend fun getUsagesNotUpdateByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getUsagesNotUpdateByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getUsagesDeletedByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getUsagesDeletedByUserId(userId).mapToDomain()
        }
    }
}
