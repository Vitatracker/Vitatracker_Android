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

}