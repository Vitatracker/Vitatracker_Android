package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class UpdateUsagesInCourseUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo
) {

    suspend operator fun invoke(usages: List<UsageCommonDomainModel>) {
        try {
            if (usages.isEmpty()) return
            //TODO("проверить нужно ли здесь удаление")
            usagesRepo.deleteUsagesByMedId(usages.first().medId)

            usagesRepo.addUsages(usages)
        } catch (e: Error) {
            Log.e("VTTAG", "UpdateUsagesInCourseUseCase: Error", e)
        }
    }

}
