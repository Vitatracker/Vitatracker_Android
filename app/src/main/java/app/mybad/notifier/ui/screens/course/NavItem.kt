package app.mybad.notifier.ui.screens.course

import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val route: String
) {
    object AddMed : NavItem("add_med")
    object AddCourse : NavItem("add_course")
    object NextCourse : NavItem("Next_course")
}