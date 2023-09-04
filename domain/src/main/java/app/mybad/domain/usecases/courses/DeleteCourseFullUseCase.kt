package app.mybad.domain.usecases.courses

import app.mybad.domain.repository.CourseRepository
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.repository.RemedyRepository
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.domain.usecases.usages.GetUseUsagesInCourseUseCase
import javax.inject.Inject

class DeleteCourseFullUseCase @Inject constructor(
    private val remedyRepository: RemedyRepository,
    private val courseRepository: CourseRepository,
    private val usageRepository: UsageRepository,
    private val patternUsageRepository: PatternUsageRepository,
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
                patternUsageRepository.markDeletionPatternUsageByCourseId(courseId = courseId)
                usageRepository.markDeletionUsagesByCourseId(courseId = courseId)

                courseRepository.getCourseById(courseId = courseId).getOrNull()?.let { course ->
                    courseRepository.markDeletionCourseById(courseId = courseId)
                    // проверить используется ли таблетка где-то еще и удалить если нет
                    val isRemedyNotUse =
                        courseRepository.getCoursesByRemedyId(remedyId = course.remedyId)
                            .getOrNull().isNullOrEmpty()
                    if (isRemedyNotUse) remedyRepository.markDeletionRemedyById(remedyId = course.remedyId)
                }
            }
        }
    }
}
