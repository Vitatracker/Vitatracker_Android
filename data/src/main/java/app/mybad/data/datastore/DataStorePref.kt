package app.mybad.data.datastore

import app.mybad.domain.models.user.*
import kotlinx.coroutines.flow.Flow

interface DataStorePref {
    suspend fun updateToken(token: String)
    suspend fun getToken()
//    val userNotification: Flow<NotificationsUserDomainModel>
//    val userPersonal: Flow<PersonalDomainModel>
//    val userRules: Flow<RulesUserDomainModel>
//    val userSettings: Flow<UserSettingsDomainModel>
//
//    suspend fun changeUserNotification(notification: NotificationsUserDomainModel)
//    suspend fun changeUserPersonal(personal: PersonalDomainModel)
//    suspend fun changeUserRules(rules: RulesUserDomainModel)
//    suspend fun changeUserSettings(settings: UserSettingsDomainModel)
}