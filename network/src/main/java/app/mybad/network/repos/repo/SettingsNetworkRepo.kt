package app.mybad.network.repos.repo

import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.response.UserModel

interface SettingsNetworkRepo {

    suspend fun getUserModel(): ApiResult

    suspend fun postUserModel(userModel: UserModel)

    suspend fun deleteUserModel()

    suspend fun putUserModel(userModel: UserModel)

}