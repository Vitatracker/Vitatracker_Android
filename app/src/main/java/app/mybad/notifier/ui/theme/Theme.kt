package app.mybad.notifier.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

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
    tertiaryContainer = tertiary30,
    onTertiaryContainer = tertiary90,
    error = error80,
    onError = error20,
    errorContainer = error30,
    onErrorContainer = error90,
    outline = neutralVariant60,
    outlineVariant = neutralVariant60,
    surfaceVariant = neutralVariant30,
    onSurfaceVariant = neutralVariant80,
    background = neutral10,
    onBackground = neutral90,
    surface = neutral10,
    onSurface = neutral80
)

private val LightColorScheme = lightColorScheme(
    primary = primary40,
    onPrimary = primary100,
    primaryContainer = primary90,
    onPrimaryContainer = primary10,
    secondary = secondary40,
    onSecondary = secondary100,
    secondaryContainer = secondary90,
    onSecondaryContainer = secondary10,
    tertiary = tertiary40,
    onTertiary = tertiary100,
    tertiaryContainer = tertiary90,
    onTertiaryContainer = tertiary10,
    error = error40,
    onError = error100,
    errorContainer = error90,
    onErrorContainer = error10,
    outline = neutralVariant50,
    outlineVariant = neutralVariant50,
    surfaceVariant = neutralVariant90,
    onSurfaceVariant = neutralVariant30,
    background = neutral99,
    onBackground = neutral10,
    surface = neutral99,
    onSurface = neutral10

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
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}