package app.mybad.data.repos

import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.data.models.usages.UsageDataModel
import app.mybad.data.room.MedDAO
import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.domain.repos.UsagesRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsagesRepoImpl @Inject constructor(
    private val db: MedDAO
) : UsagesRepo {

    override suspend fun getAll(): List<UsagesDomainModel> {
        return db.getAllUsages().mapToDomain()
    }

    override suspend fun getAllFlow(): Flow<List<UsagesDomainModel>> {
        return db.getAllUsagesFlow().map { it.mapToDomain() }
    }

    override suspend fun add(item: UsagesDomainModel) {
        db.addUsages(item.mapToData())
    }

    override suspend fun getSingle(medId: Long): UsagesDomainModel {
        return db.getUsagesByMedId(medId).mapToDomain()
    }

    override suspend fun updateSingle(medId: Long, item: UsagesDomainModel) {
        db.addUsages(item.copy(medId = medId).mapToData())
    }

    override suspend fun deleteSingle(medId: Long) {
        db.deleteUsagesByMedId(medId)
    }

    override suspend fun setUsageTime(medId: Long, usageTime: Long, factTime: Long) {
        var usages = db.getUsagesByMedId(medId)
        val list = usages.usages as MutableList
        val pos = list.indexOfLast { it.timeToUse == usageTime }
        list.removeAt(pos)
        list.add(pos, UsageDataModel(timeToUse = usageTime, usedTime = factTime))
        usages = usages.copy(usages = list)
        db.addUsages(usages)
    }
}