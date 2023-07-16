package app.mybad.domain.models.user

data class UserDomainModel(
    val id: Long = -1,
    val idn: String = "",

    val avatar: String = "",

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val name: String = "",
    val email: String = "",

    val password: String = "",
    val notUsed: Boolean = false,

    val token: String = "",
    val tokenDate: Long = 0,

    val tokenRefresh: String = "",
    val tokenRefreshDate: Long = 0,

    val updateNetworkDate: Long = 0,
    val updateLocalDate: Long = 0,
)
