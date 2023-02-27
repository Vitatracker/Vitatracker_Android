package app.mybad.data.repos

import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.repos.MedsRepo
import javax.inject.Singleton

@Singleton
class MedsRepoImpl : MedsRepo {

    override fun getAll(): List<MedDomainModel> {
        TODO("Not yet implemented")
    }

    override fun getSingle(medId: Long): MedDomainModel {
        TODO("Not yet implemented")
    }

    override fun updateSingle(medId: Long, item: MedDomainModel) {
        TODO("Not yet implemented")
    }

    override fun deleteSingle(medId: Long) {
        TODO("Not yet implemented")
    }
}