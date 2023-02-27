package app.mybad.domain.repos

import app.mybad.domain.models.med.MedDomainModel

interface MedsRepo {
    fun getAll() : List<MedDomainModel>
    fun getSingle(medId: Long) : MedDomainModel
    fun updateSingle(medId: Long, item: MedDomainModel)
    fun deleteSingle(medId: Long)
}