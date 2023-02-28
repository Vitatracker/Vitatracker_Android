package app.mybad.domain.repos

import app.mybad.domain.models.usages.UsageCommonDomainModel
import kotlinx.coroutines.flow.Flow

interface UsagesRepo {
    suspend fun getCommonAllFlow() : Flow<List<UsageCommonDomainModel>>
    suspend fun deleteSingle(medId: Long)
    suspend fun setUsageTime(medId: Long, usageTime: Long, factTime: Long)
    suspend fun getUsagesByInterval(medId: Long, startTime: Long, endTime: Long) : List<UsageCommonDomainModel>
    suspend fun getUsagesByMedId(medId: Long) : List<UsageCommonDomainModel>
    suspend fun addUsages(usages: List<UsageCommonDomainModel>)
    suspend fun deleteUsagesByMedId(medId: Long)
    suspend fun deleteUsagesByInterval(medId: Long, startTime: Long, endTime: Long)
}