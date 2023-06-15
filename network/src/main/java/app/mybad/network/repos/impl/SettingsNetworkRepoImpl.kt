package app.mybad.network.repos.impl

import app.mybad.domain.utils.ApiResult
import app.mybad.network.api.SettingsApiRepo
import app.mybad.network.models.UserModel
import app.mybad.network.repos.repo.SettingsNetworkRepo
import app.mybad.network.utils.ApiHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Named

class SettingsNetworkRepoImpl @Inject constructor(
    private val settingsApiRepo: SettingsApiRepo,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : SettingsNetworkRepo {

    override suspend fun getUserModel(): ApiResult =
        execute { settingsApiRepo.getUserModel() }

    override suspend fun postUserModel(userModel: UserModel) {
        execute { settingsApiRepo.postUserModel(userModel = userModel) }
    }

    override suspend fun deleteUserModel(id: String) {
        execute { settingsApiRepo.deleteUserModel(id = id.toLong()) }
    }

    override suspend fun putUserModel(userModel: UserModel) {
        execute { settingsApiRepo.putUserModel(userModel = userModel) }
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
