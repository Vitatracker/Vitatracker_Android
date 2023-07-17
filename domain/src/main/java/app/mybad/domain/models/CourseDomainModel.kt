package app.mybad.domain.models

data class CourseDomainModel(
    val id: Long = 0,
    var idn: Long = -1,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val comment: String = "",
    val remedyId: Long = -1,

    val startDate: Long = 0,
    val endDate: Long = 0,
    val remindDate: Long = 0,

    val interval: Long = 0,
    val regime: Int = 0,

    val isFinished: Boolean = false,
    val isInfinite: Boolean = false,
    val notUsed: Boolean = false,

    val updateNetworkDate: Long = 0,
    val updateLocalDate: Long = 0,
)
