package app.mybad.notifier.ui.screens.settings

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.user.NotificationSettingDomainModel
import app.mybad.domain.models.user.UserPersonalDomainModel
import app.mybad.domain.models.user.UserRulesDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel

sealed interface SettingsIntent {
    object DeleteAccount : SettingsIntent
    object Exit : SettingsIntent
    data class ChangePassword(val password: String) : SettingsIntent
    data class SetNotifications(val notifications: NotificationSettingDomainModel) : SettingsIntent
    data class SetPersonal(val personal: UserPersonalDomainModel) : SettingsIntent
    data class SetRules(val rules: UserRulesDomainModel) : SettingsIntent
}

data class SettingsState(
    val courses: List<CourseDomainModel> = emptyList(),
    val userModel: UserSettingsDomainModel = UserSettingsDomainModel(),
    val personalDomainModel: UserPersonalDomainModel = UserPersonalDomainModel(),
    val notificationsUserDomainModel: NotificationSettingDomainModel = NotificationSettingDomainModel(),
    val rulesUserDomainModel: UserRulesDomainModel = UserRulesDomainModel()
)
