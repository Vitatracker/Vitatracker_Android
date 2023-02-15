package app.mybad.data.models.usages

import app.mybad.data.models.usages.UsageDataModel

data class UsagesDataModel(
    val medId: String = "medId",
    val userId: String = "userId",
    val usages: List<UsageDataModel> = emptyList()
)
