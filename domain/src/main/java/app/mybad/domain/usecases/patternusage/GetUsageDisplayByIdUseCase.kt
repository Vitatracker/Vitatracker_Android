package app.mybad.domain.usecases.patternusage

import app.mybad.domain.repository.PatternUsageRepository
import javax.inject.Inject

class GetUsageDisplayByIdUseCase @Inject constructor(
    private val repository: PatternUsageRepository
) {

    suspend operator fun invoke(patternId: Long) =
        repository.getUsageDisplayById(patternId = patternId)
}
