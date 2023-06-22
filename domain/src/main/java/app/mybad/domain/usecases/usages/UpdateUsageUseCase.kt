package app.mybad.domain.usecases.usages

import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.domain.repos.CoursesNetworkRepo
import app.mybad.domain.repos.UsagesRepo
import javax.inject.Inject

class UpdateUsageUseCase @Inject constructor(
    private val usagesRepo: UsagesRepo,
    private val coursesNetworkRepo: CoursesNetworkRepo,
) {

    suspend fun execute(usage: UsageCommonDomainModel) {
        usagesRepo.updateSingle(usage)
        coursesNetworkRepo.updateUsage(usage)
    }
}
