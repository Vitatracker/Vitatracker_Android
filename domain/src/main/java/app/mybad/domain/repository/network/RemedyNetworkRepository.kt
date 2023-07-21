package app.mybad.domain.repository.network

import app.mybad.domain.models.RemedyDomainModel

interface RemedyNetworkRepository {

    suspend fun getRemedies(): Result<List<RemedyDomainModel>>

    suspend fun getRemedy(remedyId: Long): Result<RemedyDomainModel>

    suspend fun updateRemedy(remedy: RemedyDomainModel): RemedyDomainModel

    suspend fun deleteRemedy(remedyId: Long): Result<Boolean>
}
