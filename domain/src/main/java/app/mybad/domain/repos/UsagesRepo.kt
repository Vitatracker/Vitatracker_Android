package app.mybad.domain.repos

import app.mybad.domain.models.usages.UsagesDomainModel
import kotlinx.coroutines.flow.Flow

interface UsagesRepo {
    fun getAll() : List<UsagesDomainModel>
    fun getAllFlow() : Flow<List<UsagesDomainModel>>
    fun add(item: UsagesDomainModel)
    fun getSingle(medId: Long) : UsagesDomainModel
    fun updateSingle(medId: Long, item: UsagesDomainModel)
    fun deleteSingle(medId: Long)
}