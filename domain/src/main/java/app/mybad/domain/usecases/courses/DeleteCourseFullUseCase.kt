package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.domain.usecases.usages.GetUseUsagesInCourseUseCase
import javax.inject.Inject

class DeleteCourseFullUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val notificationsScheduler: NotificationsScheduler,
    private val getUseUsagesInCourseUseCase: GetUseUsagesInCourseUseCase,
    private val closeCourseUseCase: CloseCourseUseCase,
) {

    suspend operator fun invoke(courseId: Long, dateTime: Long) {
        notificationsScheduler.cancelAlarmByCourseId(courseId) {
            val isUse = getUseUsagesInCourseUseCase(courseId).getOrNull() ?: false
            if (isUse) {
                closeCourseUseCase(courseId = courseId, dateTime = dateTime)
            } else {
                usageRepository.markDeletionUsagesByCourseId(courseId = courseId)
                courseRepository.markDeletionCourseById(courseId = courseId)
            }
        }
    }
}
