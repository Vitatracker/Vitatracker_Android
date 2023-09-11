package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class UpdatePatternUsagesByCourseIdUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    suspend operator fun invoke(courseId: Long, patterns: List<PatternUsageDomainModel>) {
        try {
            repository.deletePatternUsagesByCourseId(userId = AuthToken.userId, courseId = courseId)
            if (patterns.isEmpty()) return
            repository.insertPatternUsage(patterns)
        } catch (e: Error) {
            Log.e("VTTAG", "UpdateUsagesInCourseUseCase: Error", e)
        }
    }

}

