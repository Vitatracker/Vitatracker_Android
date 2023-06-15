package app.mybad.data.models

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel

data class MyCoursesState(
    val courses: List<CourseDomainModel> = emptyList(),
    val meds: List<MedDomainModel> = emptyList(),
    val usages: List<UsageCommonDomainModel> = emptyList(),
)
