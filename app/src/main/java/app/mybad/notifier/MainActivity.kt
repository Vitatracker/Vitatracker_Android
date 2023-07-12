package app.mybad.notifier

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import app.mybad.notifier.ui.screens.authorization.StartAuthorizationScreen
import app.mybad.notifier.ui.screens.authorization.login.StartMainLoginScreen
import app.mybad.notifier.ui.screens.navigation.AppNavGraph
import app.mybad.notifier.ui.screens.navigation.Screen
import app.mybad.notifier.ui.screens.navigation.rememberNavigationState
import app.mybad.notifier.ui.screens.splash.SplashScreen
import app.mybad.notifier.ui.theme.MyBADTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MyBADTheme {
                AppNavGraph()
            }
        }
    }
}
