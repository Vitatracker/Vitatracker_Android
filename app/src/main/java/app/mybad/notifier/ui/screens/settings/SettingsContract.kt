package app.mybad.notifier.ui.screens.settings

import app.mybad.data.UserModel
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.RulesUserDomainModel
import app.mybad.domain.models.user.UserDomainModel
import kotlinx.coroutines.flow.Flow

sealed interface SettingsIntent {
    object DeleteAccount: SettingsIntent
    object Exit : SettingsIntent
    data class ChangePassword(val password: String) : SettingsIntent
    data class SetNotifications(val notifications: NotificationsUserDomainModel) : SettingsIntent

    data class SetPersonal(val personal: PersonalDomainModel) : SettingsIntent

    data class SetRules(val rules: RulesUserDomainModel) : SettingsIntent
}

data class SettingsState(
    val courses: List<CourseDomainModel> = emptyList(),
    val userModel: UserDomainModel = UserDomainModel(),
    val personalDomainModel: PersonalDomainModel = PersonalDomainModel(),
    val notificationsUserDomainModel: NotificationsUserDomainModel = NotificationsUserDomainModel(),
    val rulesUserDomainModel: RulesUserDomainModel = RulesUserDomainModel()
)