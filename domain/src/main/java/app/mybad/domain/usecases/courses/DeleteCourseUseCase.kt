package app.mybad.domain.usecases.courses

import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(
    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,
    private val medsRepo: MedsRepo,
    private val notificationsScheduler: NotificationsScheduler,
) {

    suspend fun execute(courseId: Long) {
        val medId = coursesRepo.getSingle(courseId).medId
        val now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
        notificationsScheduler.cancelByMedId(medId) {
            usagesRepo.deleteUsagesByInterval(medId, now, now + 157766400)
            coursesRepo.deleteSingle(courseId)
            medsRepo.deleteSingle(medId)
        }
    }
}
