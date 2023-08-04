package app.mybad.domain.models

data class UsageDomainModel(
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

    val factUseTime: Long = -1,
    val useTime: Long = 0, // тут полная дата со временем, но нужно только время

    val quantity: Int = 1,

    val isDeleted: Boolean = false,
    val notUsed: Boolean = false,

    val updateNetworkDate: Long = 0,
    val updateLocalDate: Long = 0,
)
