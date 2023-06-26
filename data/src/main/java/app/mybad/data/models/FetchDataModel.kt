package app.mybad.data.models

import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.entity.MedDataModel
import app.mybad.data.db.entity.UsageCommonDataModel

data class FetchDataModel(
    val med: MedDataModel?,
    val course: CourseDataModel?,
    val usages: List<UsageCommonDataModel>?
)
