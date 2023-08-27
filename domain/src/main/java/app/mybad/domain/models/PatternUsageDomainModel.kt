package app.mybad.domain.models

data class PatternUsageDomainModel(
    val id: Long = 0,
    val idn: Long = 0,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val courseId: Long = -1,
    val courseIdn: Long = 0,

    val remedyId: Long = -1,
    val remedyIdn: Long = 0,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val timeInMinutes: Int = 0, // тут только время HH:mm в минутах
    val quantity: Int = 1,

    val updateNetworkDate: Long = 0,
    val updateLocalDate: Long = 0,
)
