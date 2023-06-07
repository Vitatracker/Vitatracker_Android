package app.mybad.data.models.user

data class UserSettingsDataModel(
    val notifications: NotificationsUserDataModel = NotificationsUserDataModel(),
    val rules: RulesUserDataModel = RulesUserDataModel()
)
