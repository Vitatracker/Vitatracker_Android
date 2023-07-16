package app.mybad.domain.repository

import app.mybad.domain.models.user.NotificationSettingDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.domain.models.user.UserRulesDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel

interface UserDataRepo {
    suspend fun updateUserNotification(notification: NotificationSettingDomainModel)

    suspend fun getUserNotification(): NotificationSettingDomainModel

    suspend fun updateUserPersonal(personal: UserPersonalDomainModel)

    suspend fun getUserPersonal(): UserPersonalDomainModel

    suspend fun updateUserRules(rules: UserRulesDomainModel)

    suspend fun getUserRules(): UserRulesDomainModel

    // api
    suspend fun getUser(): Result<UserDomainModel>

    //suspend fun postUserModel(userDomainModel: UserDomainModel)

    suspend fun deleteUser(id: String)

    suspend fun putUser(userDomainModel: UserSettingsDomainModel)
}
