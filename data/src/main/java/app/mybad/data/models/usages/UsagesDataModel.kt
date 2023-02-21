package app.mybad.data.models.usages

data class UsagesDataModel(
    val medId: String = "medId",
    val userId: String = "userId",
    val usages: List<UsageDataModel> = emptyList()
)
