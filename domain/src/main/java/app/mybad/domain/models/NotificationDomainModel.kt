package app.mybad.domain.models

data class NotificationDomainModel(
    var id: Long = 0L,
    val userId: Long,
    val isEnabled: Boolean = true,
    val type: Int = 0,
    val typeId: Long = 0,
    val date: String = "",
    val time: Long, // в UTC в toEpochMilliseconds для прямого использования в будильнике
)
