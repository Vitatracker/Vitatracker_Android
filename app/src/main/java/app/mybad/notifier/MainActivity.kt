package app.mybad.notifier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.mybad.notifier.ui.screens.authorization.passwords.MainNewPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.passwords.MainRecoveryPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.registration.MainRegistrationScreen
import app.mybad.notifier.ui.theme.MyBADTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val navController = rememberNavController()
//            MyBADTheme {
//                NewCourseNav(
//                    modifier = Modifier.padding(16.dp),
//                    navController = navController
//                )
//            }

            MyBADTheme {
                MainNewPasswordScreenAuth()
            }
        }
    }
}
