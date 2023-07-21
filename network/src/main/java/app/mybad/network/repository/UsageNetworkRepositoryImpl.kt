package app.mybad.network.repository

import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.network.UsageNetworkRepository
import app.mybad.network.api.UsageApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.mapToNet
import app.mybad.theme.utils.getCurrentDateTime
import app.mybad.theme.utils.toEpochSecond
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UsageNetworkRepositoryImpl @Inject constructor(
    private val usageApi: UsageApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UsageNetworkRepository {

    override suspend fun getUsagesByCourseId(courseId: Long) = withContext(dispatcher) {
        Result.runCatching {
            usageApi.getUsagesByCourseId(courseId).mapToDomain()
        }
    }

    override suspend fun updateUsage(usage: UsageDomainModel) = withContext(dispatcher) {
        var net = usage.mapToNet()
        net = if (net.id < 0) usageApi.addUsage(net)
        else usageApi.updateUsage(net)
        usage.copy(
            idn = net.id,
            userIdn = net.userId ?: "",
            updateNetworkDate = getCurrentDateTime().toEpochSecond(),
        )
    }

    override suspend fun deleteUsage(usageId: Long) = withContext(dispatcher) {
        Result.runCatching {
            usageApi.deleteUsage(usageId).isSuccessful
        }
    }

}
