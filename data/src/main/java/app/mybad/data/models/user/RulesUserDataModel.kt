package app.mybad.data.models.user

data class RulesUserDataModel(
    val canEdit: Boolean = false,
    val canAdd: Boolean = true,
    val canShare: Boolean = false,
    val canInvite: Boolean = true,
)
