package app.mybad.domain.models.authorization

data class AuthorizationUserRegistrationDomainModel(
    val email: String,
    val password: String,
    val name: String,
)
