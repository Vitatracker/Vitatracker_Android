package app.mybad.data.repos

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.service.autofill.UserData
import android.util.Log
import androidx.datastore.core.DataStore
import app.mybad.data.UserDataModel
import app.mybad.data.UserNotificationsDataModel
import app.mybad.data.UserPersonalDataModel
import app.mybad.data.UserRulesDataModel
import app.mybad.data.UserSettingsDataModel
import app.mybad.domain.models.user.*
import app.mybad.domain.repos.UserDataRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepoImpl @Inject constructor(
    private val dataStore_userData: DataStore<UserDataModel>,
    private val dataStore_userNotification: DataStore<UserNotificationsDataModel>,
    private val dataStore_userPersonal: DataStore<UserPersonalDataModel>,
    private val dataStore_userRules: DataStore<UserRulesDataModel>,
    private val dataStore_userSettings: DataStore<UserSettingsDataModel>
) : UserDataRepo {

    val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun updateUserNotification(notification: NotificationsUserDomainModel) {
        scope.launch {
            dataStore_userNotification.updateData { userNotification ->
                userNotification.toBuilder()
                    .setIsEnabled(notification.isEnabled)
                    .setIsFloat(notification.isFloat)
                    .setMedicalControl(notification.medicationControl)
                    .setNextCourseStart(notification.nextCourseStart)
//                    .setListOfMedId()
                    .build()
            }
        }
    }

    override suspend fun getUserNotification(): NotificationsUserDomainModel {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPersonal(personal: PersonalDomainModel) {
        scope.launch {
            dataStore_userPersonal.updateData { userPersonal ->
                userPersonal.toBuilder()
                    .setAge(personal.age)
                    .setAvatar(personal.avatar)
                    .setName(personal.name)
                    .build()
            }
        }
    }

    override suspend fun getUserPersonal(): PersonalDomainModel {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserRules(rules: RulesUserDomainModel) {
        scope.launch {
            dataStore_userRules.updateData { userRules ->
                userRules.toBuilder()
                    .setCanAdd(rules.canAdd)
                    .setCanEdit(rules.canEdit)
                    .setCanInvite(rules.canInvite)
                    .setCanShare(rules.canShare)
                    .build()
            }
        }
    }

    override suspend fun getUserRules(): RulesUserDomainModel {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserSettings(settings: UserSettingsDomainModel) {
        scope.launch {
            dataStore_userSettings.updateData { userSettings ->
                userSettings.toBuilder()
                    .setUserNotificationsDataModel(0,
                        dataStore_userNotification.updateData { userNotification ->
                            userNotification.toBuilder()
                                .setIsEnabled(settings.notifications.isEnabled)
                                .setIsFloat(settings.notifications.isFloat)
                                .setMedicalControl(settings.notifications.medicationControl)
                                .setNextCourseStart(settings.notifications.nextCourseStart)
//                                .addAllListOfMedId(settings.notifications.medsId)
                                .build()
                        }
                    ).setUserRulesDataModel(0,
                        dataStore_userRules.updateData { userRules ->
                            userRules.toBuilder()
                                .setCanAdd(settings.rules.canAdd)
                                .setCanEdit(settings.rules.canEdit)
                                .setCanInvite(settings.rules.canInvite)
                                .setCanShare(settings.rules.canShare)
                                .build()
                        })
                    .build()
            }
        }
    }

    override suspend fun getUserSettings(): UserSettingsDomainModel {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserModel(model: UserDomainModel) {
        scope.launch {
            dataStore_userData.updateData { userModel ->
                userModel.toBuilder()
                    .setId(UUID.randomUUID().toString())
                    .setCreationDate(model.creationDate)
                    .setUpdateLong(model.updateDate)
                    .setUserPersonalDataModel(0, dataStore_userPersonal.updateData { userPersonal ->
                        userPersonal.toBuilder()
                            .setAge(model.personal.age)
                            .setAvatar(model.personal.avatar)
                            .setName(model.personal.name)
                            .build()
                    })
                    .setUserSettingsDataModel(0, dataStore_userSettings.updateData { userSettings ->
                        userSettings.toBuilder()
                            .setUserNotificationsDataModel(0,
                                dataStore_userNotification.updateData { userNotification ->
                                    userNotification.toBuilder()
                                        .setIsEnabled(model.settings.notifications.isEnabled)
                                        .setIsFloat(model.settings.notifications.isFloat)
                                        .setMedicalControl(model.settings.notifications.medicationControl)
                                        .setNextCourseStart(model.settings.notifications.nextCourseStart)
//                                      .setListOfMedId()
                                        .build()
                                }
                            ).setUserRulesDataModel(0,
                                dataStore_userRules.updateData { userRules ->
                                    userRules.toBuilder()
                                        .setCanAdd(model.settings.rules.canAdd)
                                        .setCanEdit(model.settings.rules.canEdit)
                                        .setCanInvite(model.settings.rules.canInvite)
                                        .setCanShare(model.settings.rules.canShare)
                                        .build()
                                })
                            .build()
                    })
                    .build()
            }
        }
    }

    override suspend fun getUserModel(): Flow<UserDomainModel> {
        return dataStore_userData.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e("TAG", "getValueFlow: $exception")
                    emit(UserDataModel::class.java.newInstance())
                } else {
                    throw exception
                }
            }.map {
                UserDomainModel(
//                    id = dataStore_userData.data.first().id,
//                    creationDate = dataStore_userData.data.first().creationDate,
//                    updateDate = dataStore_userData.data.first().updateLong,
//                    personal = dataStore_userPersonal.data.map {  }
                )
            }
    }

//    val userPreferencesFlow: Flow<UserDataModel> = dataStore.data
//        .catch { exception ->
//            // dataStore.data throws an IOException when an error is encountered when reading data
//            if (exception is IOException) {
//                Log.e("TAG", "Error reading sort order preferences.", exception)
//                emit(UserDataModel.getDefaultInstance())
//            } else {
//                throw exception
//            }
//        }


}