package app.mybad.domain.models.user

data class UserSettingsDomainModel(
    val notifications: NotificationsUserDomainModel = NotificationsUserDomainModel(),
    val rules: RulesUserDomainModel = RulesUserDomainModel()
)
