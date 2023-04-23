package app.mybad.notifier.ui.screens.mycourses

sealed class MyCoursesNavItem(
    val route: String,
) {
    object Main : MyCoursesNavItem("my_courses_main")
    object Course : MyCoursesNavItem("my_courses_details")
}
