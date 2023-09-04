package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
import javax.inject.Inject

class SendCoursesDeletedToNetworkUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val patternUsageRepository: PatternUsageRepository,

    private val remedyNetworkRepository: RemedyNetworkRepository,
    private val courseNetworkRepository: CourseNetworkRepository,
    private val usageNetworkRepository: UsageNetworkRepository,
) {

    suspend operator fun invoke(userId: Long) {
        Log.d("VTTAG", "SynchronizationCourseWorker::SendCoursesDeletedToNetworkUseCase: Start")
        if (userId != AuthToken.userId) return
        sendUsagesDeleted(userId)
        sendPatternUsagesDeleted(userId)
        sendCoursesDeleted(userId)
        sendRemediesDeleted(userId)
    }

    private suspend fun sendUsagesDeleted(userId: Long) {
        usageRepository.getUsagesDeletedByUserId(userId).getOrNull()?.forEach { usage ->
            if (usage.idn > 0) usageNetworkRepository.deleteUsage(usage.idn).getOrThrow()
            usageRepository.deleteUsagesById(usage.id)
        }
    }

    private suspend fun sendPatternUsagesDeleted(userId: Long) {
        patternUsageRepository.getPatternUsagesDeletedByUserId(userId).getOrNull()?.forEach { pattern ->
            //TODO("если реализовывать patternUsage и factUseTimeUsage")
//            if (pattern.idn > 0) patternUsageNetworkRepository.deleteUsage(pattern.idn).getOrThrow()
            patternUsageRepository.deletePatternUsage(pattern.id)
        }
    }

    private suspend fun sendCoursesDeleted(userId: Long) {
        courseRepository.getCoursesDeletedByUserId(userId).getOrNull()
            ?.forEach { course ->
                if (course.idn > 0) courseNetworkRepository.deleteCourse(course.idn).getOrThrow()
                courseRepository.deleteCoursesById(course.id)
            }
    }

    private suspend fun sendRemediesDeleted(userId: Long) {
        remedyRepository.getRemedyDeletedByUserId(userId).getOrNull()?.forEach { remedy ->
            if (remedy.idn > 0) remedyNetworkRepository.deleteRemedy(remedy.idn).getOrThrow()
            remedyRepository.deleteRemedyById(remedy.id)
        }
    }
}
