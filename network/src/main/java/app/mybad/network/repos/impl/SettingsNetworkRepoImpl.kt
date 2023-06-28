package app.mybad.network.repos.impl

import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.repos.SettingsNetworkRepository
import app.mybad.domain.utils.ApiResult
import app.mybad.network.api.SettingsApi
import app.mybad.network.models.mapToNet
import app.mybad.network.utils.ApiHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Named

class SettingsNetworkRepoImpl @Inject constructor(
    private val settingsApi: SettingsApi,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : SettingsNetworkRepository {

    override suspend fun getUserModel(): ApiResult =
        execute { settingsApi.getUserModel() }

    override suspend fun postUserModel(userDomainModel: UserDomainModel) {
        execute { settingsApi.postUserModel(userDomainModel.mapToNet()) }
    }

    override suspend fun deleteUserModel(id: String) {
        execute { settingsApi.deleteUserModel(id = id.toLong()) }
    }

    override suspend fun putUserModel(userDomainModel: UserDomainModel) {
        execute { settingsApi.putUserModel(userDomainModel.mapToNet()) }
    }

    private suspend fun execute(request: () -> Call<*>): ApiResult = withContext(dispatcher) {
        when (val response = ApiHandler.handleApi { request.invoke().execute() }) {
            is ApiResult.ApiSuccess -> ApiResult.ApiSuccess(data = response.data)
            is ApiResult.ApiError -> ApiResult.ApiError(
                code = response.code,
                message = response.message
            )

            is ApiResult.ApiException -> ApiResult.ApiException(e = response.e)
        }
    }
}
