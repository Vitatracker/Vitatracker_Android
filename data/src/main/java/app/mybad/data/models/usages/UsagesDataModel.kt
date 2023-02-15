package app.mybad.data.models.usages

import app.mybad.data.models.usages.UsageDataModel

data class UsagesDataModel(
    val medId: String = "medid",
    val userId: String = "userid",
    val usages: List<UsageDataModel> = emptyList()
)
