package app.mybad.notifier.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorScheme = lightColorScheme(
    primary = primaryBlue,
    onPrimary = primary20,

    primaryContainer = primaryContainerBlue,
    onPrimaryContainer = primary90,

    secondary = secondary100,
    onSecondary = secondary20,

    secondaryContainer = secondary100,
    onSecondaryContainer = secondary20,

    tertiary = tertiary80,
    onTertiary = tertiary20,

    // для border Настроить напоминание
    tertiaryContainer = tertiaryBorderGray,
    onTertiaryContainer = tertiary90,

    error = errorRed,
    onError = errorRed,

    errorContainer = errorRed,
    onErrorContainer = errorRed,

    outline = neutralVariant60,
    outlineVariant = primaryIndication,

    surface = neutral99,
    onSurface = neutral10,

    surfaceVariant = secondaryContainerBlue,
    onSurfaceVariant = neutralVariant30,

    background = neutral99,
    onBackground = neutral10,
)

private val DarkColorScheme = darkColorScheme(
    primary = primary80,
    onPrimary = primary20,

    primaryContainer = primary30,
    onPrimaryContainer = primary90,

    secondary = secondary80,
    onSecondary = secondary20,

    secondaryContainer = secondary30,
    onSecondaryContainer = secondary90,

    tertiary = tertiary80,
    onTertiary = tertiary20,

    // для border Настроить напоминание
    tertiaryContainer = tertiaryBorderBlue,
    onTertiaryContainer = tertiary90,

    error = error80,
    onError = error20,

    errorContainer = error30,
    onErrorContainer = error90,

    outline = neutralVariant60,
    outlineVariant = neutralVariant60,

    surface = neutral10,
    onSurface = neutral80,

    surfaceVariant = neutralVariant30,
    onSurfaceVariant = neutralVariant80,

    background = neutral10,
    onBackground = neutral90,
)

@Composable
fun MyBADTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        val systemUiController = rememberSystemUiController()
        DisposableEffect(systemUiController, darkTheme) {
            systemUiController.setSystemBarsColor(
                color = colorScheme.surface,
                darkIcons = !darkTheme
            )
            onDispose { }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
