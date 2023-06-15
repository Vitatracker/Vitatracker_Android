package app.mybad.data.repos

import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.data.db.dao.MedDao
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.repos.MedsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class MedsRepoImpl @Inject constructor(
    private val db: MedDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : MedsRepo {

    override suspend fun getAll(): List<MedDomainModel> = withContext(dispatcher) {
        db.getAllMeds().mapToDomain()
    }

    override fun getAllFlow(userId: Long): Flow<List<MedDomainModel>> =
        db.getAllMedsFlow(userId)
            .map { it.mapToDomain() }
            .flowOn(dispatcher)

    override suspend fun getSingle(medId: Long): MedDomainModel = withContext(dispatcher) {
        db.getMedById(medId).mapToDomain()
    }

    override suspend fun add(med: MedDomainModel) {
        withContext(dispatcher) {
            db.addMed(med.mapToData())
        }
    }

    override suspend fun updateSingle(medId: Long, item: MedDomainModel) {
        withContext(dispatcher) {
            db.addMed(item.copy(id = medId).mapToData())
        }
    }

    override suspend fun deleteSingle(medId: Long) {
        withContext(dispatcher) {
            db.deleteMed(medId)
        }
    }

    override suspend fun getFromList(listMedsId: List<Long>): List<MedDomainModel> =
        withContext(dispatcher) {
            db.getMedByList(listId = listMedsId).mapToDomain()
        }
}
