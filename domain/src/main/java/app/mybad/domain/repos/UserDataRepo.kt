package app.mybad.domain.repos

import app.mybad.domain.models.user.*
import app.mybad.domain.utils.ApiResult

interface UserDataRepo {
    suspend fun updateUserNotification(notification: NotificationsUserDomainModel)

    suspend fun getUserNotification(): NotificationsUserDomainModel

    suspend fun updateUserPersonal(personal: PersonalDomainModel)

    suspend fun getUserPersonal(): PersonalDomainModel

    suspend fun updateUserRules(rules: RulesUserDomainModel)

    suspend fun getUserRules(): RulesUserDomainModel

    //api
    suspend fun getUserModel(): ApiResult

    suspend fun postUserModel(userDomainModel: UserDomainModel)

    suspend fun deleteUserModel()

    suspend fun putUserModel(userDomainModel: UserDomainModel)
}
