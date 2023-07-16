package app.mybad.data.models

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel

data class MyCoursesState(
    val courses: List<CourseDomainModel> = emptyList(),
    val remedies: List<RemedyDomainModel> = emptyList(),
    val usages: List<UsageDomainModel> = emptyList(),
)
