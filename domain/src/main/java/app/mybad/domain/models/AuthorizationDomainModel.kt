package app.mybad.domain.models

data class AuthorizationDomainModel(
    val token: String,
    val refreshToken: String,
)
