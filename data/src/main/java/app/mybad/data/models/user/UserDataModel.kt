package app.mybad.data.models.user

data class UserDataModel(
    val id: Long = 0L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val personal: PersonalDataModel = PersonalDataModel(),
    val settings: UserSettingsDataModel = UserSettingsDataModel(),
)
