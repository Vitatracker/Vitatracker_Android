package app.mybad.domain.usecases.courses

import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(
    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,
    private val medsRepo: MedsRepo,
    private val notificationsScheduler: NotificationsScheduler,
) {

    suspend fun execute(courseId: Long, time: Long) {
        val medId = coursesRepo.getSingle(courseId).medId
        notificationsScheduler.cancelByMedId(medId) {
            usagesRepo.deleteUsagesAfter(medId, time)
            coursesRepo.deleteSingle(courseId)
            medsRepo.deleteSingle(medId)
        }
    }
}
