package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel

sealed interface MyCoursesIntent {
    data class Delete(val courseId: Long) : MyCoursesIntent
    data class Update(val courseId: Long, val updatedCourse: CourseDomainModel) : MyCoursesIntent
}

data class MyCoursesState(
    val courses: List<CourseDomainModel> = emptyList(),
    val meds: List<MedDomainModel> = emptyList(),
    val usages: List<UsageCommonDomainModel> = emptyList(),
)