package app.mybad.data.repos

import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
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

    override fun getAll(): List<UsagesDomainModel> {
        return db.getAllUsages().mapToDomain()
    }

    override fun getAllFlow(): Flow<List<UsagesDomainModel>> {
        return db.getAllUsagesFlow().map { it.mapToDomain() }
    }

    override fun add(item: UsagesDomainModel) {
        db.addUsages(item.mapToData())
    }

    override fun getSingle(medId: Long): UsagesDomainModel {
        return db.getUsagesByMedId(medId).mapToDomain()
    }

    override fun updateSingle(medId: Long, item: UsagesDomainModel) {
        db.addUsages(item.copy(medId = medId).mapToData())
    }

    override fun deleteSingle(medId: Long) {
        db.deleteUsagesByMedId(medId)
    }
}