package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel

sealed interface MyCoursesIntent {
    data class Delete(val courseId: Long) : MyCoursesIntent
    data class Update(
        val course: CourseDomainModel,
        val med: MedDomainModel,
        val usagesPattern: List<Pair<Long, Int>>
    ) : MyCoursesIntent
}
