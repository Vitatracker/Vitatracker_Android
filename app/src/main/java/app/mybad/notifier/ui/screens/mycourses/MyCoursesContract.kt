package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.CourseDomainModel
import app.mybad.domain.models.RemedyDomainModel

sealed interface MyCoursesIntent {
    data class Delete(val courseId: Long) : MyCoursesIntent
    data class Update(
        val course: CourseDomainModel,
        val remedy: RemedyDomainModel,
        val usagesPattern: List<Pair<Long, Int>>
    ) : MyCoursesIntent
}
