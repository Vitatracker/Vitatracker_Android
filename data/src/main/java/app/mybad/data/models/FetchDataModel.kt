package app.mybad.data.models

import app.mybad.data.db.models.CourseModel
import app.mybad.data.db.models.RemedyModel
import app.mybad.data.db.models.UsageModel

data class FetchDataModel(
    val med: RemedyModel?,
    val course: CourseModel?,
    val usages: List<UsageModel>?
)
