package app.mybad.domain.usecases.meds

import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.repos.MedsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoadMedsFromList @Inject constructor(
    private val medsRepo: MedsRepo
) {

    suspend fun execute(listMedsId: List<Long>) =
        medsRepo.getFromList(listMedsId = listMedsId)

}