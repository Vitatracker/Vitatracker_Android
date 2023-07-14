package app.mybad.data.repos

import androidx.datastore.core.DataStore
import app.mybad.data.db.dao.UsersDao
import app.mybad.data.mapToDomain
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.repos.SettingsNetworkRepository
import app.mybad.domain.repos.UserDataRepo
import app.mybad.domain.utils.ApiResult
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
    private val db: UsersDao,
) : UserDataRepo {

    override suspend fun updateUserNotification(notification: NotificationsUserDomainModel) {
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

    override suspend fun getUserNotification(): NotificationsUserDomainModel =
        withContext(dispatcher) {
            userNotification.data.first().mapToDomain()
        }

    override suspend fun updateUserPersonal(personal: PersonalDomainModel) {
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

    override suspend fun getUserPersonal(): PersonalDomainModel = withContext(dispatcher) {
        userPersonal.data.first().mapToDomain()
    }

    override suspend fun updateUserRules(rules: RulesUserDomainModel) {
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

    override suspend fun getUserRules(): RulesUserDomainModel = withContext(dispatcher) {
        userRules.data.first().mapToDomain()
    }

    // api
    override suspend fun getUserModel(): ApiResult = withContext(dispatcher) {
        settingsNetworkRepo.getUserModel()
    }

//    override suspend fun postUserModel(userDomainModel: UserDomainModel) {
//        settingsNetworkRepo.postUserModel(userModel = userDomainModel.mapToNetwork())
//    }

    override suspend fun deleteUserModel(id: String) {
        withContext(dispatcher) {
            settingsNetworkRepo.deleteUserModel(id = id)
        }
    }

    override suspend fun putUserModel(userDomainModel: UserDomainModel) {
        withContext(dispatcher) {
            settingsNetworkRepo.putUserModel(userDomainModel)
        }
    }

    override suspend fun getUsersCount(): Int = withContext(dispatcher) {
        db.getUsersCount()
    }
}
