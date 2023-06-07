package app.mybad.network.repos.repo

import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.UserModel

interface SettingsNetworkRepo {

    suspend fun getUserModel(): ApiResult

    suspend fun postUserModel(userModel: UserModel)

    suspend fun deleteUserModel(id: String)

    suspend fun putUserModel(userModel: UserModel)
}
