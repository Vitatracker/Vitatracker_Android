package app.mybad.domain.models.usages

data class UsagesDomainModel(
    val medId: Long = -1L,
    val userId: String = "userid",
    val usages: List<UsageDomainModel> = emptyList()
)
