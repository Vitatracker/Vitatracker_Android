package app.mybad.domain.repos

import app.mybad.domain.models.usages.UsagesDomainModel

interface UsagesRepo {
    fun getAll() : List<UsagesDomainModel>
    fun add(item: UsagesDomainModel)
    fun getSingle(medId: Long) : UsagesDomainModel
    fun updateSingle(medId: Long, item: UsagesDomainModel)
    fun deleteSingle(medId: Long)
}