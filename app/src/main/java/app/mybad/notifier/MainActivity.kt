package app.mybad.notifier

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mybad.notifier.ui.navigation.AppNavGraph
import app.mybad.notifier.ui.navigation.NavigationState
import app.mybad.notifier.ui.navigation.rememberNavigationState
import app.mybad.notifier.ui.theme.MyBADTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navState: NavigationState
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle(false)
             MyBADTheme(darkTheme = isDarkTheme, dynamicColor = false) {
                navState = rememberNavigationState()
                AppNavGraph(navState)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navState.navController.handleDeepLink(intent)
    }
}
