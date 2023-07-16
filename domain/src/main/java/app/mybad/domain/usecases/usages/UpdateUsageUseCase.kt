package app.mybad.domain.usecases.usages

import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.CourseNetworkRepository
import javax.inject.Inject

class UpdateUsageUseCase @Inject constructor(
    private val usageRepository: UsageRepository,
    private val coursesNetworkRepo: CourseNetworkRepository,
) {

    suspend operator fun invoke(usage: UsageDomainModel) {
        usageRepository.insertUsage(usage)
        //TODO("сделать обновление через воркер")
//        coursesNetworkRepo.updateUsage(usage)
    }
}
