package app.mybad.domain.usecases.courses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class AddNotificationsUseCase @Inject constructor(
    private val repository: NotificationsScheduler,
) {

    suspend operator fun invoke(
        course: CourseDomainModel,
        usages: List<UsageCommonDomainModel>
    ) {
        repository.add(usages)
        repository.add(course)
    }
}
