package app.mybad.data.repos

import androidx.datastore.core.DataStore
import app.mybad.data.mapToDomain
import app.mybad.domain.models.user.NotificationSettingDomainModel
import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.domain.models.user.UserRulesDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.repository.UserDataRepo
import app.mybad.domain.repository.network.SettingsNetworkRepository
import app.vitatracker.data.UserNotificationsDataModel
import app.vitatracker.data.UserPersonalDataModel
import app.vitatracker.data.UserRulesDataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class UserDataRepoImpl @Inject constructor(
    private val userNotification: DataStore<UserNotificationsDataModel>,
    private val userPersonal: DataStore<UserPersonalDataModel>,
    private val userRules: DataStore<UserRulesDataModel>,
    private val settingsNetworkRepo: SettingsNetworkRepository,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : UserDataRepo {

    override suspend fun updateUserNotification(notification: NotificationSettingDomainModel) {
        withContext(dispatcher) {
            userNotification.updateData { notification ->
                notification.toBuilder()
                    .setIsEnabled(notification.isEnabled)
                    .setIsFloat(notification.isFloat)
                    .setMedicationControl(notification.medicationControl)
                    .setNextCourseStart(notification.nextCourseStart)
//                    .setListOfMedId()
                    .build()
            }
        }
    }

    override suspend fun getUserNotification(): NotificationSettingDomainModel =
        withContext(dispatcher) {
            userNotification.data.first().mapToDomain()
        }

    override suspend fun updateUserPersonal(personal: UserPersonalDomainModel) {
        withContext(dispatcher) {
            userPersonal.updateData { personal ->
                personal.toBuilder()
                    .setAge(personal.age ?: "")
                    .setAvatar(personal.avatar ?: "")
                    .setName(personal.name ?: "")
                    .setEmail(personal.email ?: "")
                    .build()
            }
        }
    }

    override suspend fun getUserPersonal(): UserPersonalDomainModel = withContext(dispatcher) {
        userPersonal.data.first().mapToDomain()
    }

    override suspend fun updateUserRules(rules: UserRulesDomainModel) {
        withContext(dispatcher) {
            userRules.updateData { userRules ->
                userRules.toBuilder()
                    .setCanAdd(rules.canAdd)
                    .setCanEdit(rules.canEdit)
                    .setCanInvite(rules.canInvite)
                    .setCanShare(rules.canShare)
                    .build()
            }
        }
    }

    override suspend fun getUserRules() = withContext(dispatcher) {
        userRules.data.first().mapToDomain()
    }

    // api
    override suspend fun getUser() = withContext(dispatcher) {
        settingsNetworkRepo.getUser()
    }

//    override suspend fun postUserModel(userDomainModel: UserDomainModel) {
//        settingsNetworkRepo.postUserModel(userModel = userDomainModel.mapToNetwork())
//    }

    override suspend fun deleteUser(id: String) {
        withContext(dispatcher) {
            settingsNetworkRepo.deleteUser(id = id)
        }
    }

    override suspend fun putUser(userDomainModel: UserSettingsDomainModel) {
        withContext(dispatcher) {
            settingsNetworkRepo.updateUser(userDomainModel)
        }
    }
}
