package app.mybad.domain.models.usages

data class UsagesDomainModel(
    val medId: String = "medId",
    val userId: String = "userId",
    val usages: List<UsageDomainModel> = emptyList()
)
