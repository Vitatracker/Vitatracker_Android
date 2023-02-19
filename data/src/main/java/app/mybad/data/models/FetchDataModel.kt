package app.mybad.data.models

import app.mybad.data.models.course.CourseDataModel
import app.mybad.data.models.med.MedDataModel
import app.mybad.data.models.usages.UsagesDataModel

data class FetchDataModel(
    val med: MedDataModel?,
    val course: CourseDataModel?,
    val usages: UsagesDataModel?
)
