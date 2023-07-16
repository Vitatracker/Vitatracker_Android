package app.mybad.notifier.ui.screens.newcourse

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.domain.models.UsageDomainModel

sealed interface NewCourseIntent {
    object Drop : NewCourseIntent
    object Finish : NewCourseIntent
    data class UpdateMed(val remedy: RemedyDomainModel) : NewCourseIntent
    data class UpdateCourse(val course: CourseDomainModel) : NewCourseIntent
    data class UpdateUsagesPattern(val pattern: List<Pair<Long, Int>>) : NewCourseIntent
    data class UpdateUsages(val usages: List<UsageDomainModel>) : NewCourseIntent
}

data class NewCourseState(
    val remedy: RemedyDomainModel = RemedyDomainModel(),
    val course: CourseDomainModel = CourseDomainModel(),
    val usages: List<UsageDomainModel> = emptyList(),
//    val userId: Long = 0L
)
