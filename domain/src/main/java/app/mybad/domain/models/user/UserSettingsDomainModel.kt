package app.mybad.domain.models.user

data class UserSettingsDomainModel(
    val id: Long = 0L,
    val createdDate: Long = 0L,
    val updatedDate: Long = 0L,
    val personal: UserPersonalDomainModel = UserPersonalDomainModel(),
    val settings: UserNotificationDomainModel = UserNotificationDomainModel()
)
