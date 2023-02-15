package app.mybad.data.models.user

data class NotificationsUserDataModel(
    val isEnabled: Boolean = true,
    val isFloat: Boolean = false,
    val medicationControl: Boolean = false,
    val nextCourseStart: Boolean = true,
    val medsId: List<Long> = emptyList(),
)