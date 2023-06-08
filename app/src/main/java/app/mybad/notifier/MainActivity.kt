package app.mybad.notifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mybad.notifier.ui.screens.authorization.AuthorizationScreenViewModel
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationScreenNavHost
import app.mybad.notifier.ui.screens.calender.CalendarViewModel
import app.mybad.notifier.ui.screens.newcourse.CreateCourseViewModel
import app.mybad.notifier.ui.screens.mainscreen.StartMainScreenViewModel
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.screens.navigation.MainNav
import app.mybad.notifier.ui.screens.settings.SettingsViewModel
import app.mybad.notifier.ui.theme.MyBADTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private val authorizationScreenViewModel: AuthorizationScreenViewModel by viewModels()
    private val createCourseVm: CreateCourseViewModel by viewModels()
    private val myCoursesVm: MyCoursesViewModel by viewModels()
    private val settingsVm: SettingsViewModel by viewModels()
    private val calendarVm: CalendarViewModel by viewModels()
    private val mainScreenVm: StartMainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState = mainActivityViewModel.uiState.collectAsStateWithLifecycle()

//            mainActivityViewModel.clearDataStore()

            MyBADTheme {
                if (uiState.value.token.isEmpty()) {
                    AuthorizationScreenNavHost(
                        authVM = authorizationScreenViewModel,
                        mainVM = mainActivityViewModel
                    )
                } else {
                    MainNav(
                        createCourseVm = createCourseVm,
                        myCoursesVm = myCoursesVm,
                        settingsVm = settingsVm,
                        calendarVm = calendarVm,
                        mainScreenVm = mainScreenVm,
                        mainVM = mainActivityViewModel
                    )
                }
            }
        }
    }
}
