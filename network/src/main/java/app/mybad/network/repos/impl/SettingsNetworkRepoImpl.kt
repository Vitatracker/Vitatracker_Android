package app.mybad.network.repos.impl

import app.mybad.domain.utils.ApiResult
import app.mybad.network.api.SettingsApiRepo
import app.mybad.network.models.UserModel
import app.mybad.network.repos.repo.SettingsNetworkRepo
import app.mybad.network.utils.ApiHandler
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsNetworkRepoImpl @Inject constructor(
    private val settingsApiRepo: SettingsApiRepo
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

    private suspend fun execute(request: () -> Call<*>): ApiResult {
        return when (val response = ApiHandler.handleApi { request.invoke().execute() }) {
            is ApiResult.ApiSuccess -> ApiResult.ApiSuccess(data = response.data)
            is ApiResult.ApiError -> ApiResult.ApiError(
                code = response.code,
                message = response.message
            )

            is ApiResult.ApiException -> ApiResult.ApiException(e = response.e)
        }
    }
}
