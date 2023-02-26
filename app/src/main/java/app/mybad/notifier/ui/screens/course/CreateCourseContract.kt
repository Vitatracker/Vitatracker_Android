package app.mybad.notifier.ui.screens.course

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel

sealed interface CreateCourseIntent {
    object Drop : CreateCourseIntent
    object Finish : CreateCourseIntent
    data class NewMed(val med: MedDomainModel) : CreateCourseIntent
    data class NewCourse(val course: CourseDomainModel) : CreateCourseIntent
    data class NewUsages(val usages: UsagesDomainModel) : CreateCourseIntent
}

data class CreateCourseState(
    val med: MedDomainModel = MedDomainModel(),
    val course: CourseDomainModel = CourseDomainModel(),
    val usages: UsagesDomainModel = UsagesDomainModel()
)