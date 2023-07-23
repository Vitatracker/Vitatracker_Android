package app.mybad.domain.models.authorization

data class RegistrationCreateAccountErrorDomainModel(
    val violations: List<Violation>
)

data class Violation(
    val fieldName: String,
    val message: String
)