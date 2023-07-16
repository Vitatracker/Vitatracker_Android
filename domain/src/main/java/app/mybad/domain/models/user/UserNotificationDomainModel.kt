package app.mybad.domain.models.user

data class UserNotificationDomainModel(
    val notifications: NotificationSettingDomainModel = NotificationSettingDomainModel(),
    val rules: UserRulesDomainModel = UserRulesDomainModel()
)
