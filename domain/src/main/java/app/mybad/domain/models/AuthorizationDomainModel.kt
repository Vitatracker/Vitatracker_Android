package app.mybad.domain.models

data class AuthorizationDomainModel(
    val token: String = "",
    val tokenDate: Long = 0,
    val tokenRefresh: String = "",
    val tokenRefreshDate: Long = 0,
    val message: String = "",
)
