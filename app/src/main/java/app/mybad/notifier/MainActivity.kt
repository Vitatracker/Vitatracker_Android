package app.mybad.notifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import app.mybad.notifier.ui.screens.course.NewCourseNav
import app.mybad.notifier.ui.screens.settings.SettingsNav
import app.mybad.notifier.ui.screens.authorization.navigation.AuthorizationScreenNavHost
import app.mybad.notifier.ui.theme.MyBADTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MyBADTheme {
                SettingsNav(
                    navController = navController,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
