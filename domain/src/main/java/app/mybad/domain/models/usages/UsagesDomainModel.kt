package app.mybad.domain.models.usages

data class UsagesDomainModel(
    val medId: String = "medid",
    val userId: String = "userid",
    val usages: List<UsageDomainModel> = emptyList()
)
