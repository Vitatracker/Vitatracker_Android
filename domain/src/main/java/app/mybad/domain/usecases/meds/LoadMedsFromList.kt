package app.mybad.domain.usecases.meds

import app.mybad.domain.repos.MedsRepo
import javax.inject.Inject

class LoadMedsFromList @Inject constructor(
    private val medsRepo: MedsRepo
) {

    suspend fun execute(listMedsId: List<Long>) =
        medsRepo.getFromList(listMedsId = listMedsId)
}
