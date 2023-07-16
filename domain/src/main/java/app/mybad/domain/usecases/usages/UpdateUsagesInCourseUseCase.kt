package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class UpdateUsagesInCourseUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    suspend operator fun invoke(usages: List<UsageDomainModel>) {
        try {
            if (usages.isEmpty()) return
            //TODO("проверить нужно ли здесь удаление")
            repository.deleteUsagesById(usages.first().courseId)

            repository.insertUsages(usages)
        } catch (e: Error) {
            Log.e("VTTAG", "UpdateUsagesInCourseUseCase: Error", e)
        }
    }

}
