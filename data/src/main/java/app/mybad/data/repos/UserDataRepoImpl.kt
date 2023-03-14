package app.mybad.data.repos

import androidx.datastore.core.DataStore
import app.mybad.data.UserNotificationsDataModel
import app.mybad.data.UserPersonalDataModel
import app.mybad.data.UserRulesDataModel
import app.mybad.data.mapToDomain
import app.mybad.domain.models.user.*
import app.mybad.domain.repos.UserDataRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepoImpl @Inject constructor(
    private val dataStore_userNotification: DataStore<UserNotificationsDataModel>,
    private val dataStore_userPersonal: DataStore<UserPersonalDataModel>,
    private val dataStore_userRules: DataStore<UserRulesDataModel>
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
//        val notification = dataStore_userNotification.data.first()
        return dataStore_userNotification.data.last().mapToDomain()
//        return dataStore_userNotification.data.map {
//            NotificationsUserDomainModel(
//                isEnabled = notification.isEnabled,
//                isFloat = notification.isFloat,
//                medicationControl = notification.medicalControl,
//                nextCourseStart = notification.nextCourseStart,
////                medsId = notification.getListOfMedId(0)
//            )
//        }
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
//        val personal = dataStore_userPersonal.data.first()
//        return dataStore_userPersonal.data.map {
//            PersonalDomainModel(
//                name = personal.name,
//                age = personal.age,
//                avatar = personal.avatar
//            )
//        }
        return dataStore_userPersonal.data.last().mapToDomain()
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
//        val rules = dataStore_userRules.data.first()
//        return dataStore_userRules.data.map {
//            RulesUserDomainModel(
//                canAdd = rules.canAdd,
//                canEdit = rules.canEdit,
//                canInvite = rules.canInvite,
//                canShare = rules.canShare
//            )
//        }
        return dataStore_userRules.data.last().mapToDomain()
    }

}