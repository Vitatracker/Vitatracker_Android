package app.mybad.domain.repos

import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.utils.ApiResult

interface SettingsNetworkRepository {

    suspend fun getUserModel(): ApiResult

    suspend fun postUserModel(userDomainModel: UserDomainModel)

    suspend fun deleteUserModel(id: String)

    suspend fun putUserModel(userDomainModel: UserDomainModel)
}
