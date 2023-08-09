package app.mybad.domain.repository

import app.mybad.domain.models.RemedyDomainModel
import kotlinx.coroutines.flow.Flow

interface RemedyRepository {
    fun getRemedies(userId: Long): Flow<List<RemedyDomainModel>>
    suspend fun getRemediesByUserId(userId: Long): Result<List<RemedyDomainModel>>
    suspend fun getRemedyById(remedyId: Long): Result<RemedyDomainModel>
    suspend fun getRemedyByIdn(remedyIdn: Long): Result<RemedyDomainModel>
    suspend fun insertRemedy(remedy: RemedyDomainModel): Result<Long?>
    suspend fun insertRemedy(remedies: List<RemedyDomainModel>): Result<Unit>
    suspend fun updateRemedy(remedy: RemedyDomainModel): Result<Long?>
    suspend fun delete(remedyId: Long, dateTime: Long): Result<Unit>
    suspend fun deleteRemedyById(remedyId: Long): Result<Unit>
    suspend fun deleteRemedies(remedies: List<RemedyDomainModel>): Result<Unit>
    suspend fun getRemedyNotUpdateByUserId(userId: Long): Result<List<RemedyDomainModel>>
    suspend fun getRemedyDeletedByUserId(userId: Long): Result<List<RemedyDomainModel>>
    //TODO("это зачем?")
    suspend fun getRemediesByIds(remedyIdList: List<Long>): Result<List<RemedyDomainModel>>
}
