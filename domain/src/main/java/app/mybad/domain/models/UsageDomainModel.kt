package app.mybad.domain.models

data class UsageDomainModel(
    val id: Long = 0,
    val idn: Long = -1,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val courseId: Long = -1,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val factUseTime: Long = -1,
    val useTime: Long = 0,

    val quantity: Int = 1,

    val isDeleted: Boolean = false,
    val notUsed: Boolean = false,

    val updateNetworkDate: Long = 0,
    val updateLocalDate: Long = 0,
)
