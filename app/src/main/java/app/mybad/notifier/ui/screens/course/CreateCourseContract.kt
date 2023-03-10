package app.mybad.notifier.ui.screens.course

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import java.time.LocalTime

sealed interface NewCourseIntent {
    object Drop : NewCourseIntent
    object Finish : NewCourseIntent
    data class UpdateMed(val med: MedDomainModel) : NewCourseIntent
    data class UpdateCourse(val course: CourseDomainModel) : NewCourseIntent
    data class UpdateUsagesPattern(val pattern: List<Pair<LocalTime, Int>>) : NewCourseIntent
    data class UpdateUsages(val usages: List<UsageCommonDomainModel>) : NewCourseIntent
}

data class NewCourseState(
    val med: MedDomainModel = MedDomainModel(),
    val course: CourseDomainModel = CourseDomainModel(),
    val usages: List<UsageCommonDomainModel> = emptyList(),
    val userId: String = "userid"
)