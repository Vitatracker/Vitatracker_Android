package app.mybad.domain.usecases.user

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
import javax.inject.Inject

class ClearDBUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val remedyNetworkRepository: RemedyNetworkRepository,
    private val courseNetworkRepository: CourseNetworkRepository,
    private val usageNetworkRepository: UsageNetworkRepository,
) {

    suspend operator fun invoke() {
        remedyNetworkRepository.getRemedies().getOrNull()?.forEach { remedy ->
            remedyNetworkRepository.deleteRemedy(remedy.idn)
        }
        courseNetworkRepository.getCourses().getOrNull()?.forEach { course ->
            courseNetworkRepository.deleteCourse(course.idn)
        }
        usageNetworkRepository.getUsages().getOrNull()?.forEach { usage ->
            usageNetworkRepository.deleteUsage(usage.idn)
        }
        AuthToken.userId.takeIf { it > 0 }?.let { userId ->
            remedyRepository.getRemediesByUserId(userId).getOrNull()?.forEach { remedy ->
                remedyRepository.deleteRemedyById(remedy.id)
            }
            courseRepository.getCoursesByUserId(userId).getOrNull()?.forEach { course ->
                courseRepository.deleteCourseById(course.id)
            }
            usageRepository.getUsagesByUserId(userId).getOrNull()?.forEach { usage ->
                usageRepository.deleteUsagesById(usage.id)
            }
        }
    }
}
