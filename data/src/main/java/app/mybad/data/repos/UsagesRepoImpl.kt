package app.mybad.data.repos

import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Singleton

@Singleton
class UsagesRepoImpl : UsagesRepo {

    override fun getAll(): List<UsagesDomainModel> {
        TODO("Not yet implemented")
    }

    override fun getSingle(medId: Long): UsagesDomainModel {
        TODO("Not yet implemented")
    }

    override fun updateSingle(medId: Long, item: UsagesDomainModel) {
        TODO("Not yet implemented")
    }

    override fun deleteSingle(medId: Long) {
        TODO("Not yet implemented")
    }
}