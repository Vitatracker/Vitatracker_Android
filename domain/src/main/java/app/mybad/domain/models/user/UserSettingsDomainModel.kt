package app.mybad.domain.models.user

data class UserSettingsDomainModel(
    val id: Long = 0L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val personal: UserPersonalDomainModel = UserPersonalDomainModel(),
    val settings: UserNotificationDomainModel = UserNotificationDomainModel()
)
