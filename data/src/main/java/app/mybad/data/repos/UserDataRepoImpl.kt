package app.mybad.data.repos

import androidx.datastore.core.DataStore
import app.mybad.data.mapToDomain
import app.mybad.data.mapToNetwork
import app.mybad.domain.models.user.*
import app.mybad.domain.repos.UserDataRepo
import app.mybad.domain.utils.ApiResult
import app.mybad.network.repos.repo.SettingsNetworkRepo
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepoImpl @Inject constructor(
    private val dataStore_userNotification: DataStore<UserNotificationsDataModel>,
    private val dataStore_userPersonal: DataStore<UserPersonalDataModel>,
    private val dataStore_userRules: DataStore<UserRulesDataModel>,
    private val settingsNetworkRepo: SettingsNetworkRepo
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
        return dataStore_userNotification.data.first().mapToDomain()
    }

    override suspend fun updateUserPersonal(personal: PersonalDomainModel) {
        scope.launch {
            dataStore_userPersonal.updateData { userPersonal ->
                userPersonal.toBuilder()
                    .setAge(if (personal.age == null) "" else personal.age)
                    .setAvatar(if (personal.avatar == null) "" else personal.avatar)
                    .setName(if (personal.name == null) "" else personal.name)
                    .setEmail(if (personal.email == null) "" else personal.email)
                    .build()
            }
        }
    }

    override suspend fun getUserPersonal(): PersonalDomainModel {
        return dataStore_userPersonal.data.first().mapToDomain()
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
        return dataStore_userRules.data.first().mapToDomain()
    }

    // api
    override suspend fun getUserModel(): ApiResult {
        return settingsNetworkRepo.getUserModel()
    }

    override suspend fun postUserModel(userDomainModel: UserDomainModel) {
        settingsNetworkRepo.postUserModel(userModel = userDomainModel.mapToNetwork())
    }

    override suspend fun deleteUserModel() {
        settingsNetworkRepo.deleteUserModel()
    }

    override suspend fun putUserModel(userDomainModel: UserDomainModel) {
        settingsNetworkRepo.putUserModel(userModel = userDomainModel.mapToNetwork())
    }
}
