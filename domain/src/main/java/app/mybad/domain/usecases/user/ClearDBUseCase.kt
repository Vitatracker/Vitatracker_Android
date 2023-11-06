package app.mybad.domain.usecases.user

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.NotificationRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class ClearDBUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val patternUsageRepository: PatternUsageRepository,

    private val notificationsScheduler: NotificationsScheduler,
    private val notificationRepository: NotificationRepository,

    private val remedyNetworkRepository: RemedyNetworkRepository,
    private val courseNetworkRepository: CourseNetworkRepository,
    private val usageNetworkRepository: UsageNetworkRepository,
) {

    suspend operator fun invoke() {
        Log.w("VTTAG", "ClearDBUseCase:: delete usageNetworkRepository")
        // удаляем на сервере
        usageNetworkRepository.getUsages().getOrNull()?.forEach { usage ->
            usageNetworkRepository.deleteUsage(usage.idn)
        }
        Log.w("VTTAG", "ClearDBUseCase:: delete courseNetworkRepository")
        courseNetworkRepository.getCourses().getOrNull()?.forEach { course ->
            courseNetworkRepository.deleteCourse(course.idn)
        }
        Log.w("VTTAG", "ClearDBUseCase:: delete remedyNetworkRepository")
        remedyNetworkRepository.getRemedies().getOrNull()?.forEach { remedy ->
            remedyNetworkRepository.deleteRemedy(remedy.idn)
        }
        Log.w("VTTAG", "ClearDBUseCase:: local userId=${AuthToken.userId} ")
        // удаляем в локальной базе
        AuthToken.userId.takeIf { it > 0 }?.let { userId ->
            Log.w("VTTAG", "ClearDBUseCase:: stop notification")
            notificationsScheduler.cancelAlarm(userId)
            Log.w("VTTAG", "ClearDBUseCase:: delete notificationRepository")
            notificationRepository.deleteNotificationByUserId(userId)

            Log.w("VTTAG", "ClearDBUseCase:: delete patternUsageRepository")
            patternUsageRepository.deletePatternUsagesByUserId(userId)
            Log.w("VTTAG", "ClearDBUseCase:: delete usageRepository")
            usageRepository.deleteUsagesByUserId(userId)
            Log.w("VTTAG", "ClearDBUseCase:: delete courseRepository")
            courseRepository.deleteCoursesByUserId(userId)
            Log.w("VTTAG", "ClearDBUseCase:: delete remedyRepository")
            remedyRepository.deleteRemediesByUserId(userId)
        }
    }
}
