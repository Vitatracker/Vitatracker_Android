package app.mybad.network.repository

import android.util.Log
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.network.UsageNetworkRepository
import app.mybad.network.api.UsageApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.mapToNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UsageNetworkRepositoryImpl @Inject constructor(
    private val usageApi: UsageApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UsageNetworkRepository {

    override suspend fun getUsages() = withContext(dispatcher) {
        Result.runCatching {
            usageApi.getUsages().mapToDomain()
        }
    }

    override suspend fun getUsagesByCourseId(
        courseId: Long,
        courseIdLoc: Long,
        remedyIdNet: Long,
        remedyIdLoc: Long,
    ) = withContext(dispatcher) {
        Result.runCatching {
            usageApi.getUsagesByCourseId(courseId)
                .mapToDomain(
                    remedyIdLoc = remedyIdLoc,
                    remedyIdNet = remedyIdNet,
                    courseIdLoc = courseIdLoc,
                )
        }
    }

    override suspend fun updateUsage(usage: UsageDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::UsageNetworkRepositoryImpl: usage id=${usage.id}"
            )
            var usageNet = usage.mapToNet()
            usageNet = if (usageNet.id > 0) usageApi.updateUsage(usageNet)
            else usageApi.addUsage(usageNet)
            usageNet.mapToDomain(
                remedyIdLoc = usage.remedyId,
                remedyIdNet = usage.remedyIdn,
                courseIdLoc = usage.courseId,
                usageIdLoc = usage.id,
            )
        }
    }

    override suspend fun deleteUsage(usageId: Long) = withContext(dispatcher) {
        Result.runCatching {
            usageApi.deleteUsage(usageId).isSuccessful
        }
    }

}
