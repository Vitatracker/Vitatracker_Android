package app.mybad.notifier.ui.screens.mycourses

import app.mybad.domain.models.course.CourseDomainModel

sealed interface MyCoursesIntent {

}

data class MyCoursesState(
    val courses: List<CourseDomainModel> = emptyList()
)