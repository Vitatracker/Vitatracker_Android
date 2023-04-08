package app.mybad.notifier.ui.screens.newcourse

sealed class NewCourseNavItem(val route: String) {
    object AddMedicineFirst : NewCourseNavItem("add_medicine_1")
    object AddMedicineSecond : NewCourseNavItem("add_medicine_2")
    object AddCourse : NewCourseNavItem("add_course")
    object AddNotifications : NewCourseNavItem("add_notifications")
    object Success : NewCourseNavItem("add_success")
}