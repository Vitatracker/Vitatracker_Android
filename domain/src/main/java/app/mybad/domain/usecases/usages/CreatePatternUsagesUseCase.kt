package app.mybad.domain.usecases.usages

import app.mybad.domain.models.PatternUsageDomainModel
import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class CreatePatternUsagesUseCase @Inject constructor(
    private val repository: PatternUsageRepository,
) {

    suspend operator fun invoke(patterns: List<PatternUsageDomainModel>) = repository.insertPatternUsage(patterns)
}
