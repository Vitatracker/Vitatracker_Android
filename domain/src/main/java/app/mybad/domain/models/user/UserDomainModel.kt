package app.mybad.domain.models.user

data class UserDomainModel(
    val id: String = "userId",
    val personal: PersonalDomainModel = PersonalDomainModel(),
    val settings: UserSettingsDomainModel = UserSettingsDomainModel(),
)