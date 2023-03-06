package app.mybad.notifier

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import app.mybad.notifier.ui.screens.calender.CalendarViewModel
import app.mybad.notifier.ui.screens.course.CreateCourseViewModel
import app.mybad.notifier.ui.screens.mainscreen.StartMainScreenViewModel
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.screens.navigation.MainNav
import app.mybad.notifier.ui.screens.settings.SettingsViewModel
import app.mybad.notifier.ui.theme.MyBADTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val createCourseVm : CreateCourseViewModel by viewModels()
    private val myCoursesVm: MyCoursesViewModel by viewModels()
    private val settingsVm : SettingsViewModel by viewModels()
    private val calendarVm : CalendarViewModel by viewModels()
    private val mainScreenVm : StartMainScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MyBADTheme {
                MainNav(
                    navController = navController,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    createCourseVm = createCourseVm,
                    myCoursesVm = myCoursesVm,
                    settingsVm = settingsVm,
                    calendarVm = calendarVm,
                    mainScreenVm = mainScreenVm
                )
            }
        }
    }
}
