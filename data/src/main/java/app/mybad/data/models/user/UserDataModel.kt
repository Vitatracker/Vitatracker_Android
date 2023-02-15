package app.mybad.data.models.user

data class UserDataModel(
    val id: String = "userid",
    val personal: PersonalDataModel = PersonalDataModel(),
    val settings: UserSettingsDataModel = UserSettingsDataModel(),
)