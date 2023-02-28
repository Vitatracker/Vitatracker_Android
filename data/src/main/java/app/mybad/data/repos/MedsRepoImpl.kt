package app.mybad.data.repos

import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.repos.MedsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedsRepoImpl @Inject constructor() : MedsRepo {

    override fun getAll(): List<MedDomainModel> {
        return listOf(
            MedDomainModel(id=1L, name = "Doliprane",   details = MedDetailsDomainModel(type = 1, dose = 500, measureUnit = 1, icon = 0)),
            MedDomainModel(id=2L, name = "Dexedrine",   details = MedDetailsDomainModel(type = 1, dose = 30,  measureUnit = 1, icon = 0)),
            MedDomainModel(id=3L, name = "Prozac",      details = MedDetailsDomainModel(type = 1, dose = 120, measureUnit = 1, icon = 0)),
        )
    }

    override fun getSingle(medId: Long): MedDomainModel {
        return MedDomainModel()
    }

    override fun add(med: MedDomainModel) {

    }

    override fun updateSingle(medId: Long, item: MedDomainModel) {
    }

    override fun deleteSingle(medId: Long) {
    }
}