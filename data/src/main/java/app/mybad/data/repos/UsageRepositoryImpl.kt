package app.mybad.data.repos

import android.util.Log
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
        Result.runCatching {
            db.getUsagesByUserId(userId).mapToDomain()
        }
    }

    override suspend fun getUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            db.getUsagesById(courseId).mapToDomain()
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

    override suspend fun deleteUsagesById(courseId: Long) {
        withContext(dispatcher) {
            try {
                db.deleteUsagesById(courseId)
            } catch (ignore: Throwable) {
            }
        }
    }

    //TODO("не понятно что тут происходит")
    override suspend fun updateUsageFactTimeById(courseId: Long, usageTime: Long, factTime: Long) {
        withContext(dispatcher) {
            try {
                db.getUsagesById(courseId)
                    .lastOrNull { it.courseId == courseId && it.useTime == usageTime }
                    ?.copy(factUseTime = factTime)?.let {
                        db.insertUsage(it)
                    }
            } catch (ignore: Throwable) {
            }
        }
    }

    override suspend fun insertUsage(usage: UsageDomainModel) {
        withContext(dispatcher) {
            try {
                db.insertUsage(usage.mapToData())
            } catch (ignore: Throwable) {
                Log.w("VTTAG", "UsageRepositoryImpl::insertUsage: error=${ignore.localizedMessage}")
            }
        }
    }

    override suspend fun insertUsages(usages: List<UsageDomainModel>) {
        withContext(dispatcher) {
            try {
                db.insertUsages(usages.mapToData())
            } catch (ignore: Throwable) {
                Log.w("VTTAG", "UsageRepositoryImpl::insertUsages: error=${ignore.localizedMessage}")
            }
        }
    }

    override suspend fun deleteUsagesBetweenById(courseId: Long, startTime: Long, endTime: Long) {
        withContext(dispatcher) {
            try {
                db.deleteUsagesBetweenById(courseId, startTime, endTime)
            } catch (ignore: Throwable) {
            }
        }
    }

    override suspend fun deleteUsagesAfter(courseId: Long, afterTime: Long) {
        withContext(dispatcher) {
            try {
                db.deleteUsagesAfter(courseId, afterTime)
            } catch (ignore: Throwable) {
            }
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
}
