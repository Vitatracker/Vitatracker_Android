package app.mybad.domain.repos

import app.mybad.domain.models.usages.UsagesDomainModel
import kotlinx.coroutines.flow.Flow

interface UsagesRepo {
    suspend fun getAll() : List<UsagesDomainModel>
    suspend fun getAllFlow() : Flow<List<UsagesDomainModel>>
    suspend fun add(item: UsagesDomainModel)
    suspend fun getSingle(medId: Long) : UsagesDomainModel
    suspend fun updateSingle(medId: Long, item: UsagesDomainModel)
    suspend fun deleteSingle(medId: Long)
    suspend fun setUsageTime(medId: Long, usageTime: Long, factTime: Long)
}