package app.mybad.domain.repos

import app.mybad.domain.models.user.*
import kotlinx.coroutines.flow.Flow

interface UserDataRepo {
    suspend fun updateUserNotification(notification: NotificationsUserDomainModel)

    suspend fun getUserNotification(): NotificationsUserDomainModel

    suspend fun updateUserPersonal(personal: PersonalDomainModel)

    suspend fun getUserPersonal(): PersonalDomainModel

    suspend fun updateUserRules(rules: RulesUserDomainModel)

    suspend fun getUserRules(): RulesUserDomainModel

    suspend fun updateUserSettings(settings: UserSettingsDomainModel)

    suspend fun getUserSettings(): UserSettingsDomainModel

    suspend fun updateUserModel(userModel: UserDomainModel)

    suspend fun getUserModel(): Flow<UserDomainModel>

}