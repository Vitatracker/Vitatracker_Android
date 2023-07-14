package app.mybad.notifier.ui.screens.navigation

sealed class AddCourseScreens(val route: String) {
    object MedDetailsScreen : AddCourseScreens(ROUTE_MED_DETAILS)
    object MedReceptionScreen : AddCourseScreens(ROUTE_MED_RECEPTION)
    object CourseDetailsScreen : AddCourseScreens(ROUTE_COURSE_DETAILS)
    object CourseDurationScreen : AddCourseScreens(ROUTE_COURSE_DURATION)
    object CongratulationsScreen : AddCourseScreens(ROUTE_CONGRATULATIONS)

    companion object {
        private const val ROUTE_MED_DETAILS = "add_course_med_details"
        private const val ROUTE_MED_RECEPTION = "add_course_med_reception"
        private const val ROUTE_COURSE_DETAILS = "add_course_details"
        private const val ROUTE_COURSE_DURATION = "add_course_duration"
        private const val ROUTE_CONGRATULATIONS = "add_course_congrats"
    }
}