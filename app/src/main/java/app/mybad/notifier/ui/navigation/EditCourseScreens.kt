package app.mybad.notifier.ui.navigation

sealed class EditCourseScreens(val route: String) {
    object CourseEdit : EditCourseScreens(ROUTE_MED_DETAILS)
    object NotificationEdit : EditCourseScreens(ROUTE_MED_RECEPTION)

    companion object {
        private const val ROUTE_MED_DETAILS = "my_course_edit"
        private const val ROUTE_MED_RECEPTION = "my_course_edit_notification"
    }
}
