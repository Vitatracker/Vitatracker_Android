package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel

sealed interface MyCoursesIntent {

}

data class MyCoursesState(
    val courses: List<CourseDomainModel> = emptyList(),
    val meds: List<MedDomainModel> = emptyList(),
)