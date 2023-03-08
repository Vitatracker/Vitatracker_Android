package app.mybad.data.datastore

import app.mybad.data.models.user.NotificationsUserDataModel
import app.mybad.data.models.user.PersonalDataModel
import app.mybad.data.models.user.RulesUserDataModel
import app.mybad.data.models.user.UserSettingsDataModel
import app.mybad.domain.models.user.*
import kotlinx.coroutines.flow.Flow

interface DataStoreProto {
    suspend fun updateUserNotification(notification: NotificationsUserDataModel)

    suspend fun getUserNotification()

    suspend fun updateUserPersonal(personal: PersonalDataModel)

    suspend fun getUserPersonal()

    suspend fun updateUserRules(rules: RulesUserDataModel)

    suspend fun getUserRules()

    suspend fun updateUserSettings(settings: UserSettingsDataModel)

    suspend fun getUserSettings()
}