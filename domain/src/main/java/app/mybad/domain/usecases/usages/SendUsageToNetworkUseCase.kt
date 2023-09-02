package app.mybad.domain.usecases.usages

import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
import javax.inject.Inject

class SendUsageToNetworkUseCase @Inject constructor(
    private val usageRepository: UsageRepository,
    private val usageNetworkRepository: UsageNetworkRepository,
) {

    suspend operator fun invoke(userId: Long, usageId: Long) = runCatching {
        if (userId != AuthToken.userId) error("error usage synchronize")
        usageRepository.getUsageById(usageId).getOrNull()?.let { usage ->
            usageNetworkRepository.updateUsage(usage).isSuccess
        } ?: false
    }
}
