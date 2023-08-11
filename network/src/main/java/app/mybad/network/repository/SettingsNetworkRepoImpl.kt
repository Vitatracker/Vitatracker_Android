package app.mybad.network.repository

import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.repository.network.SettingsNetworkRepository
import app.mybad.network.api.SettingsApi
import app.mybad.network.models.mapToDomain
import app.mybad.network.models.mapToNet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

//TODO("не понятно что это и для чего")
class SettingsNetworkRepoImpl @Inject constructor(
    private val settingsApi: SettingsApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : SettingsNetworkRepository {

    override suspend fun getUser() = withContext(dispatcher) {
        runCatching {
            //TODO("проверить что тут получать")
            settingsApi.getUser("").mapToDomain()
        }
    }

    override suspend fun postUser(userDomainModel: UserSettingsDomainModel) {
        withContext(dispatcher) {
            runCatching {
                settingsApi.addUser(userDomainModel.mapToNet())
            }
        }
    }

    override suspend fun deleteUser(id: String) {
        withContext(dispatcher) {
            runCatching {
                settingsApi.deleteUser(id = id)
            }
        }
    }

    override suspend fun updateUser(userDomainModel: UserSettingsDomainModel) {
        withContext(dispatcher) {
            runCatching {
                settingsApi.updateUser(userDomainModel.mapToNet())
            }
        }
    }
}
