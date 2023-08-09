package app.mybad.domain.models

data class AuthorizationDomainModel(
    val token: String,
    val tokenDate: Long,
    val tokenRefresh: String,
    val tokenRefreshDate: Long,
)
