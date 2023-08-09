package app.mybad.domain.usecases.courses

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
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
    private val remedyNetworkRepository: RemedyNetworkRepository,
    private val courseNetworkRepository: CourseNetworkRepository,
    private val usageNetworkRepository: UsageNetworkRepository,

    ) {

    suspend operator fun invoke() {
        Log.d("VTTAG", "SynchronizationCourseWorker::SendCoursesDeletedToNetworkUseCase: Start")
        sendUsagesDeleted()
        sendCoursesDeleted()
        sendRemediesDeleted()
    }

    private suspend fun sendUsagesDeleted() {
        usageRepository.getUsagesDeletedByUserId(AuthToken.userId).getOrNull()?.forEach { usage ->
            usageNetworkRepository.deleteUsage(usage.idn).getOrThrow()
            usageRepository.deleteUsagesById(usage.id)
        }
    }

    private suspend fun sendCoursesDeleted() {
        courseRepository.getCoursesDeletedByUserId(AuthToken.userId).getOrNull()
            ?.forEach { course ->
                courseNetworkRepository.deleteCourse(course.idn).getOrThrow()
                courseRepository.deleteCourseById(course.id)
            }
    }

    private suspend fun sendRemediesDeleted() {
        remedyRepository.getRemedyDeletedByUserId(AuthToken.userId).getOrNull()?.forEach { remedy ->
            remedyNetworkRepository.deleteRemedy(remedy.idn).getOrThrow()
            remedyRepository.deleteRemedyById(remedy.id)
        }
    }
}
