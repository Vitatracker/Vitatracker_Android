package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class GetUsagesUseCase @Inject constructor(
    private val repository: UsageRepository
) {

    suspend operator fun invoke() = repository.getUsagesByCourseId(AuthToken.userId)
}
