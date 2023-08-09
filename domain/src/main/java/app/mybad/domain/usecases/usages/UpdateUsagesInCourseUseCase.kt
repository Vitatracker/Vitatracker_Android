package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class UpdateUsagesInCourseUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    suspend operator fun invoke(courseId: Long, usages: List<UsageDomainModel>) {
        try {
            repository.deleteUsagesByCourseId(courseId)
            if (usages.isEmpty()) return
            repository.insertUsage(usages)
        } catch (e: Error) {
            Log.e("VTTAG", "UpdateUsagesInCourseUseCase: Error", e)
        }
    }

}
