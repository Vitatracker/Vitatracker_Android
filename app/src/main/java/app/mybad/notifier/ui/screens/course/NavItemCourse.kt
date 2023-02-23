package app.mybad.notifier.ui.screens.course

import androidx.annotation.StringRes
import app.mybad.notifier.R

sealed class NavItemCourse(
    val route: String,
    @StringRes val stringId: Int
) {
    object AddMed : NavItemCourse("add_med", R.string.add_med_h)
    object AddCourse : NavItemCourse("add_course", R.string.add_course_h)
    object NextCourse : NavItemCourse("Next_course", R.string.add_course_h)
    object CourseCreated : NavItemCourse("Created", R.string.add_course_finish)
}