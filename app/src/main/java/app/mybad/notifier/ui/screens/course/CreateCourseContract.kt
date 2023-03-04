package app.mybad.notifier.ui.screens.course

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel

sealed interface CreateCourseIntent {
    object Drop : CreateCourseIntent
    object Finish : CreateCourseIntent
    data class NewMed(val med: MedDomainModel) : CreateCourseIntent
    data class NewCourse(val course: CourseDomainModel) : CreateCourseIntent
    data class NewUsages(val usages: List<UsageCommonDomainModel>) : CreateCourseIntent
}

data class CreateCourseState(
    val med: MedDomainModel = MedDomainModel(),
    val course: CourseDomainModel = CourseDomainModel(),
    val usages: List<UsageCommonDomainModel> = emptyList(),
    val userId: String = "userid"
)