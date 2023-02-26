package app.mybad.notifier

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import app.mybad.notifier.ui.screens.course.CreateCourseViewModel
import app.mybad.notifier.ui.screens.navigation.MainNav
import app.mybad.notifier.ui.theme.MyBADTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val createCourseVm : CreateCourseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MyBADTheme {
                MainNav(
                    navController = navController,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    createCourseVm = createCourseVm
                )
            }
        }
    }
}
