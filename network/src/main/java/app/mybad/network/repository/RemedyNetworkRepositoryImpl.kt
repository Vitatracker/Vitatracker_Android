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
        runCatching {
            remedyApi.getRemedies().mapToDomain()
        }
    }

    override suspend fun getRemedy(remedyId: Long) = withContext(dispatcher) {
        runCatching {
            remedyApi.getRemedy(remedyId).mapToDomain()
        }
    }

    override suspend fun updateRemedy(remedy: RemedyDomainModel) = withContext(dispatcher) {
        runCatching {
            Log.d(
                "VTTAG",
                "SynchronizationCourseWorker::RemedyNetworkRepositoryImpl:updateRemedy: remedy id=${remedy.id}"
            )
            var remedyNet = remedy.mapToNet()
            remedyNet = if (remedyNet.id > 0) {
                // TODO("перед тем как отправить обновление, нужно проверить не удалено ли на сервере через web, проверить есть ли на сервере, если нет, то нужно удалить")
                val isNotDeleted = try {
                    remedyApi.getRemedy(remedyNet.id).id > 0
                } catch (ignore: Error) {
                    false
                }
                if (isNotDeleted) {
                    remedyApi.updateRemedy(remedyNet)
                } else {
                    TODO("надо удалить и в локальной базе")
                    error("remedyNet is deleted")
                }
            } else remedyApi.addRemedy(remedyNet)
            remedyNet.mapToDomain(remedyIdLoc = remedy.id)
        }
    }

    override suspend fun deleteRemedy(remedyId: Long) = withContext(dispatcher) {
        runCatching {
            remedyApi.deleteRemedy(remedyId).isSuccessful
        }
    }

}
