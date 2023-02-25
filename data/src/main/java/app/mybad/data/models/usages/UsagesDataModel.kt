package app.mybad.data.models.usages

data class UsagesDataModel(
    val medId: Long = -1L,
    val creationDate: Long = 0L,
    val userId: String = "userid",
    val needControl: Boolean = false,
    val usages: List<UsageDataModel> = emptyList()
)
