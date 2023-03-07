package app.mybad.domain.usecases

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.MedsRepo
import app.mybad.domain.repos.UsagesRepo
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(
    private val medsRepo: MedsRepo,
    private val coursesRepo: CoursesRepo,
    private val usagesRepo: UsagesRepo,
    private val notificationsScheduler: NotificationsScheduler
) {

    suspend fun execute(
        med: MedDomainModel,
        course: CourseDomainModel,
        usages: List<UsageCommonDomainModel>
    ) {
        medsRepo.add(med)
        coursesRepo.add(course.copy(medId = med.id))
        usagesRepo.addUsages(usages)
        notificationsScheduler.add(usages)
    }

}