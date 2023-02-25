package app.mybad.domain.models.usages

data class UsagesDomainModel(
    val medId: Long = -1L,
    val creationDate: Long = 0L,
    val userId: String = "userid",
    val needControl: Boolean = false,
    val usages: List<UsageDomainModel> = emptyList()
)
