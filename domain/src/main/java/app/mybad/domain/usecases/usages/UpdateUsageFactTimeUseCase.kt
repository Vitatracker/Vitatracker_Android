package app.mybad.domain.usecases.usages

import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class UpdateUsageFactTimeUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    suspend operator fun invoke(courseId: Long, usageTime: Long, factTime: Long) {
        repository.updateUsageFactTimeById(
            courseId = courseId,
            usageTime = usageTime,
            factTime = factTime
        )
    }
}
