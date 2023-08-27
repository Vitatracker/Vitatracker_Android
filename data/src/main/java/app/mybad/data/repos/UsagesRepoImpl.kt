package app.mybad.data.repos

import android.util.Log
import app.mybad.data.db.dao.MedDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.UsagesRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UsagesRepoImpl @Inject constructor(
    private val db: MedDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UsagesRepo {

    override fun getCommonAllFlow(userId: Long): Flow<List<UsageCommonDomainModel>> =
        db.getAllCommonUsagesFlow(userId).map { it.mapToDomain() }.flowOn(dispatcher)

    override suspend fun getCommonAll(userId: Long): List<UsageCommonDomainModel> =
        withContext(dispatcher) {
            db.getAllCommonUsagesFlow(userId).first().mapToDomain()
        }

    override suspend fun deleteSingle(medId: Long) {
        withContext(dispatcher) {
            db.deleteUsagesById(medId)
        }
    }

    override suspend fun setUsageTime(medId: Long, usageTime: Long, factTime: Long) {
        withContext(dispatcher) {
            val usages = db.getUsagesById(medId) as MutableList
            val pos = usages.indexOfLast { it.medId == medId && it.useTime == usageTime }
            val temp = usages[pos]
            usages.removeAt(pos)
            usages.add(pos, temp.copy(factUseTime = factTime))
            db.addUsages(usages)
        }
    }

    override suspend fun getUsagesByIntervalByMed(
        medId: Long,
        startTime: Long,
        endTime: Long
    ) = withContext(dispatcher) {
        db.getUsagesByIntervalByMed(medId, startTime, endTime).mapToDomain()
    }

    override suspend fun getUsagesByMedId(medId: Long) = withContext(dispatcher) {
        db.getUsagesById(medId).mapToDomain()
    }

    override suspend fun addUsages(usages: List<UsageCommonDomainModel>) {
        withContext(dispatcher) {
            Log.d("VTTAG", "UsagesRepoImpl addUsages $usages")
            db.addUsages(usages.mapToData())
        }
    }

    override suspend fun updateSingle(usage: UsageCommonDomainModel) {
        withContext(dispatcher) {
            db.updateSingleUsage(usage.mapToData())
        }
    }

    override suspend fun deleteUsagesByMedId(medId: Long) {
        withContext(dispatcher) {
            db.deleteUsagesById(medId)
        }
    }

    override suspend fun deleteUsagesByInterval(medId: Long, startTime: Long, endTime: Long) {
        withContext(dispatcher) {
            db.deleteUsagesByInterval(medId, startTime, endTime)
        }
    }

    override suspend fun deleteUsagesAfter(medId: Long, time: Long) {
        withContext(dispatcher) {
            db.deleteUsagesAfter(medId, time)
        }
    }

    override suspend fun getUsagesByInterval(
        startTime: Long,
        endTime: Long
    ) = withContext(dispatcher) {
        db.getUsagesByInterval(startTime = startTime, endTime = endTime).mapToDomain()
    }
}
