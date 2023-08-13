package app.mybad.domain.repository.network

import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel

interface SettingsNetworkRepository {

    suspend fun getUser(): Result<UserDomainModel>

    suspend fun postUser(userDomainModel: UserSettingsDomainModel)

    suspend fun deleteUser(id: String): Result<Boolean>

    suspend fun updateUser(userDomainModel: UserSettingsDomainModel)
}
