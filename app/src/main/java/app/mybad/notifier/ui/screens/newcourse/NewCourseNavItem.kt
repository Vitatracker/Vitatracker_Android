package app.mybad.notifier.ui.screens.newcourse

sealed class NewCourseNavItem(val route: String) {
    object AddMedicineMain : NewCourseNavItem("add_medicine")
    object AddMedicineDetails : NewCourseNavItem("add_medicine_details")
    object AddCourse : NewCourseNavItem("add_course")
    object AddNotifications : NewCourseNavItem("add_notifications")
    object Success : NewCourseNavItem("add_success")
}