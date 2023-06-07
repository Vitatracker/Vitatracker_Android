package app.mybad.domain.models.user

data class UserDomainModel(
    val id: Long = 0L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val personal: PersonalDomainModel = PersonalDomainModel(),
    val settings: UserSettingsDomainModel = UserSettingsDomainModel()
)
