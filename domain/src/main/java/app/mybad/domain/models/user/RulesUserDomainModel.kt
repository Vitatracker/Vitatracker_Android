package app.mybad.domain.models.user

data class RulesUserDomainModel(
    val canEdit: Boolean = false,
    val canAdd: Boolean = true,
    val canShare: Boolean = false,
    val canInvite: Boolean = true,
)
