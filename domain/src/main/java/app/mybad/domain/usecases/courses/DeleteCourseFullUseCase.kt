package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class DeleteCourseFullUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val remedyRepository: RemedyRepository,
    private val notificationsScheduler: NotificationsScheduler,
) {

    suspend operator fun invoke(courseId: Long, time: Long) {
        notificationsScheduler.cancelAlarmByCourseId(courseId) {
            courseRepository.getCourseById(courseId).getOrNull()?.let { course ->
                usageRepository.deleteUsagesAfter(
                    courseId = course.id,
                    afterTime = time
                )
                remedyRepository.deleteRemedyById(course.remedyId)
            }
            courseRepository.deleteCourseById(courseId)
        }
    }
}
