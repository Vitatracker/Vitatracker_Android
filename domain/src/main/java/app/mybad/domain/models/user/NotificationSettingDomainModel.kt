package app.mybad.domain.models.user

data class NotificationSettingDomainModel(
    val isEnabled: Boolean = true,
    val isFloat: Boolean = false,
    val medicationControl: Boolean = false,
    val nextCourseStart: Boolean = true,
    val medsId: List<Long> = emptyList(),
)
