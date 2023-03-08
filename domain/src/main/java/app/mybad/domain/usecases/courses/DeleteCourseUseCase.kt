package app.mybad.domain.usecases.courses

import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import java.time.Instant
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(
    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,
    private val notificationsScheduler: NotificationsScheduler
) {

    suspend fun execute(courseId: Long) {
        val medId = coursesRepo.getSingle(courseId).medId
        val now = Instant.now().epochSecond
        coursesRepo.deleteSingle(courseId)
        notificationsScheduler.cancelByMedId(medId)
        usagesRepo.deleteUsagesByInterval(medId, now, now+157766400)
    }
}