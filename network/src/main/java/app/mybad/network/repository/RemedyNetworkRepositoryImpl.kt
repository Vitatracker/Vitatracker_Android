package app.mybad.network.repository

import android.util.Log
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.repository.network.RemedyNetworkRepository
import app.mybad.network.api.RemedyApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.mapToNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class RemedyNetworkRepositoryImpl @Inject constructor(
    private val remedyApi: RemedyApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : RemedyNetworkRepository {
    override suspend fun getRemedies() = withContext(dispatcher) {
        Result.runCatching {
            remedyApi.getRemedies().mapToDomain()
        }
    }

    override suspend fun getRemedy(remedyId: Long) = withContext(dispatcher) {
        Result.runCatching {
            remedyApi.getRemedy(remedyId).mapToDomain()
        }
    }

    override suspend fun updateRemedy(remedy: RemedyDomainModel) = withContext(dispatcher) {
        Result.runCatching {
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::RemedyNetworkRepositoryImpl:updateRemedy: remedy id=${remedy.id}"
            )
            var remedyNet = remedy.mapToNet()
            remedyNet = if (remedyNet.id > 0) remedyApi.updateRemedy(remedyNet)
            else remedyApi.addRemedy(remedyNet)
            remedyNet.mapToDomain(remedy.id)
        }
    }

    override suspend fun deleteRemedy(remedyId: Long) = withContext(dispatcher) {
        Result.runCatching {
            remedyApi.deleteRemedy(remedyId).isSuccessful
        }
    }

}
