package app.mybad.data.repos

import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.data.room.MedDAO
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.UsagesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsagesRepoImpl @Inject constructor(
    private val db: MedDAO
) : UsagesRepo {

    override suspend fun getCommonAllFlow(): Flow<List<UsageCommonDomainModel>> {
        return db.getAllCommonUsagesFlow().map { it.mapToDomain() }
    }

    override suspend fun deleteSingle(medId: Long) {
        db.deleteUsagesById(medId)
    }

    override suspend fun setUsageTime(medId: Long, usageTime: Long, factTime: Long) {
        val usages = db.getUsagesById(medId) as MutableList
        val pos = usages.indexOfLast { it.medId == medId && it.useTime == usageTime }
        val temp = usages[pos]
        usages.removeAt(pos)
        usages.add(pos, temp.copy(factUseTime = factTime))
        db.addUsages(usages)
    }

    override suspend fun getUsagesByIntervalByMed(
        medId: Long,
        startTime: Long,
        endTime: Long
    ): List<UsageCommonDomainModel> {
        return db.getUsagesByIntervalByMed(medId, startTime, endTime).mapToDomain()
    }

    override suspend fun getUsagesByMedId(medId: Long): List<UsageCommonDomainModel> {
        return db.getUsagesById(medId).mapToDomain()
    }

    override suspend fun addUsages(usages: List<UsageCommonDomainModel>) {
        db.addUsages(usages.mapToData())
    }

    override suspend fun updateSingle(usage: UsageCommonDomainModel) {
        db.updateSingleUsage(usage.mapToData())
    }

    override suspend fun deleteUsagesByMedId(medId: Long) {
        db.deleteUsagesById(medId)
    }

    override suspend fun deleteUsagesByInterval(medId: Long, startTime: Long, endTime: Long) {
        db.deleteUsagesByInterval(medId, startTime, endTime)
    }

    override suspend fun getUsagesByInterval(
        startTime: Long,
        endTime: Long
    ): List<UsageCommonDomainModel> {
        return db.getUsagesByInterval(startTime = startTime, endTime = endTime).mapToDomain()
    }
}