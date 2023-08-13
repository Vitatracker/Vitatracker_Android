package app.mybad.data.models

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel

data class EditCourseState(
    val course: CourseDomainModel = CourseDomainModel(),
    val remedy: RemedyDomainModel = RemedyDomainModel(),
    val usagesPattern: List<UsageFormat> = emptyList(),
)
