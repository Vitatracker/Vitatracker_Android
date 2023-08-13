package app.mybad.notifier.ui.navigation

sealed class NewCourseScreens(val route: String) {
    object MedDetailsScreen : NewCourseScreens(ROUTE_MED_DETAILS)
    object MedReceptionScreen : NewCourseScreens(ROUTE_MED_RECEPTION)
    object CourseDetailsScreen : NewCourseScreens(ROUTE_COURSE_DETAILS)
    object CourseDurationScreen : NewCourseScreens(ROUTE_COURSE_DURATION)
    object CongratulationsScreen : NewCourseScreens(ROUTE_CONGRATULATIONS)

    companion object {
        private const val ROUTE_MED_DETAILS = "add_course_med_details"
        private const val ROUTE_MED_RECEPTION = "add_course_med_reception"
        private const val ROUTE_COURSE_DETAILS = "add_course_details"
        private const val ROUTE_COURSE_DURATION = "add_course_duration"
        private const val ROUTE_CONGRATULATIONS = "add_course_congrats"
    }
}
