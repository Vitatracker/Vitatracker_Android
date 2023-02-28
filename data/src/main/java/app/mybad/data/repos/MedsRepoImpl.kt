package app.mybad.data.repos

import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.data.room.MedDAO
import app.mybad.domain.models.med.MedDetailsDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.repos.MedsRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedsRepoImpl @Inject constructor(
    private val db: MedDAO
) : MedsRepo {

    override fun getAll(): List<MedDomainModel> {
        return db.getAllMeds().mapToDomain()
    }

    override fun getSingle(medId: Long): MedDomainModel {
        return db.getMedById(medId).mapToDomain()
    }

    override fun add(med: MedDomainModel) {
        db.addMed(med.mapToData())
    }

    override fun updateSingle(medId: Long, item: MedDomainModel) {
        db.addMed(item.copy(id = medId).mapToData())
    }

    override fun deleteSingle(medId: Long) {
        db.deleteMed(medId)
    }
}