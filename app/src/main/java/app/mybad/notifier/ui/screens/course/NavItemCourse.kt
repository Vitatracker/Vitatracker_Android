package app.mybad.notifier.ui.screens.course

sealed class NavItemCourse(
    val route: String
) {
    object AddMed : NavItemCourse("add_med")
    object AddCourse : NavItemCourse("add_course")
    object NextCourse : NavItemCourse("Next_course")
    object CourseCreated : NavItemCourse("Created")
}