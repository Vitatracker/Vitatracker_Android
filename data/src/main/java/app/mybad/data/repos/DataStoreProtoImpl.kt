package app.mybad.data.repos

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.mybad.data.datastore.DataStorePref
import app.mybad.data.datastore.DataStoreProto
import app.mybad.data.models.user.NotificationsUserDataModel
import app.mybad.data.models.user.PersonalDataModel
import app.mybad.data.models.user.RulesUserDataModel
import app.mybad.data.models.user.UserSettingsDataModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreProtoImpl @Inject constructor(
    private val dataStore: DataStore<PersonalDataModel>
) : DataStoreProto {

    val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun updateUserNotification(notification: NotificationsUserDataModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserNotification() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPersonal(personal: PersonalDataModel) {
//        scope.launch {
//        dataStore.updateData { user ->
//            user.copy(name = personal.name, age = personal.age, avatar = personal.avatar)
//        }
//        }
    }

    override suspend fun getUserPersonal() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserRules(rules: RulesUserDataModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserRules() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserSettings(settings: UserSettingsDataModel) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserSettings() {
        TODO("Not yet implemented")
    }

}