package app.mybad.domain.usecases.courses

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.scheduler.NotificationsScheduler
import javax.inject.Inject

class AddNotificationsUseCase @Inject constructor(
    private val repository: NotificationsScheduler,
) {

    suspend operator fun invoke(
        course: CourseDomainModel,
        usages: List<UsageDomainModel>
    ) {
        repository.addAlarm(usages)
        repository.addAlarm(course)
    }
}
