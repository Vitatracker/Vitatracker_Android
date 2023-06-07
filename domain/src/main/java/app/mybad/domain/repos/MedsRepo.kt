package app.mybad.domain.repos

import app.mybad.domain.models.med.MedDomainModel
import kotlinx.coroutines.flow.Flow

interface MedsRepo {
    suspend fun getAll(): List<MedDomainModel>
    suspend fun getAllFlow(): Flow<List<MedDomainModel>>
    suspend fun getSingle(medId: Long): MedDomainModel
    suspend fun add(med: MedDomainModel)
    suspend fun updateSingle(medId: Long, item: MedDomainModel)
    suspend fun deleteSingle(medId: Long)
    suspend fun getFromList(listMedsId: List<Long>): List<MedDomainModel>
}
