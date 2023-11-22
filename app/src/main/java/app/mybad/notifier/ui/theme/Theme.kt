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
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = accent_color,
    onPrimary = light_text,

    // BottomNavBar, карточка курса, поверхность
    primaryContainer = light_primary_container,
    onPrimaryContainer = light_text,

    secondary = light_secondary,
    onSecondary = light_text,

    // buttons, текст всегда белый
    secondaryContainer = accent_color,
    onSecondaryContainer = buttons_text,

    // AddNotificationButton
    tertiary = accent_color,
    onTertiary = tertiary_text,

    // кнопка google
    tertiaryContainer = google_buttons,
    onTertiaryContainer = google_buttons_text,

    error = error_primary,
    onError = error_primary,
    errorContainer = error_container,
    onErrorContainer = error_container,

    background = light_background,
    onBackground = light_text,

    surface = light_surface,
    onSurface = light_text,

    // AlertDialog
    surfaceVariant = light_surface_variant,
    onSurfaceVariant = accent_color,

    outline = border,
    // для border Настроить напоминание
    outlineVariant = border_variant,

    // старт курса через
    inverseSurface = light_surface_high,
    inverseOnSurface = light_surface_high_text,

    // карточка курса, border
    inversePrimary = light_border_card,

    // AlertDialog задний фон
    surfaceTint = light_surface_tint,

    scrim = seed, // холст

    // иконка редактирования курса, всегда белая и текст на темном фоне
    surfaceBright = surface_bright, //поверхность Яркая

    // MultiBox
    surfaceContainer = light_surface_container,

    // подсвечивание времени при выборе
    surfaceContainerHigh = light_surface_select, // поверхность Контейнер Высокий

    surfaceContainerHighest = settings_icon_tint, // поверхность Контейнер Высший

    surfaceContainerLow = seed, // поверхность Контейнер Низкий

    surfaceContainerLowest = seed, // поверхность Контейнер Самый низкий

    surfaceDim = placeholder_text, // поверхность Тусклый
)


private val DarkColorScheme = darkColorScheme(
    primary = accent_color,
    onPrimary = dark_text,

    primaryContainer = dark_primary_container,
    onPrimaryContainer = dark_text,

    secondary = dark_secondary,
    onSecondary = dark_text,

    secondaryContainer = accent_color,
    onSecondaryContainer = buttons_text,

    tertiary = accent_color,
    onTertiary = tertiary_text,

    tertiaryContainer = google_buttons,
    onTertiaryContainer = google_buttons_text,

    error = error_primary,
    onError = error_primary,
    errorContainer = error_container,
    onErrorContainer = error_container,

    background = dark_background,
    onBackground = dark_text,

    surface = dark_surface,
    onSurface = dark_text,

    surfaceVariant = dark_surface_variant,
    onSurfaceVariant = dark_text,

    outline = border,
    outlineVariant = border_variant,

    inverseSurface = dark_surface_high,
    inverseOnSurface = dark_surface_high_text,

    inversePrimary = dark_border_card,

    surfaceTint = dark_surface_tint,

    scrim = seed,

    surfaceBright = surface_bright, //поверхность Яркая

    surfaceContainer = dark_surface_container,

    surfaceContainerHigh = dark_surface_select, // поверхность Контейнер Высокий
    surfaceContainerHighest = settings_icon_tint, // поверхность Контейнер Высший
    surfaceContainerLow = seed, // поверхность Контейнер Низкий
    surfaceContainerLowest = seed, // поверхность Контейнер Самый низкий

    surfaceDim = placeholder_text, // поверхность Тусклый
)

/*
private val LightColorScheme = lightColorScheme(
    // buttons
    primary = primary60,
    onPrimary = primary100,

    primaryContainer = primaryContainerBlue,
    onPrimaryContainer = primary90,

    inversePrimary = primary90,

    secondary = secondary100,
    onSecondary = secondary30,

    // карточка курса, поверхность
    secondaryContainer = cardBackgroundLight,
    onSecondaryContainer = cardTextLight,

    // кнопка google
    tertiary = secondary40,
    onTertiary = secondary30,

    // для border Настроить напоминание
    tertiaryContainer = tertiaryBorderGray,
    onTertiaryContainer = tertiary90,

    surface = primary100,
    onSurface = secondary30,

    // AlertDialog
    surfaceVariant = primary100,
    onSurfaceVariant = secondary30,

    inverseSurface = secondary30,
    inverseOnSurface = secondary30,

    error = errorRed,
    onError = errorRed,

    errorContainer = errorRed,
    onErrorContainer = errorRed,

    outline = neutralVariant60,
    outlineVariant = primaryIndication,

    background = primary100,
    onBackground = secondary30,

    scrim = primary100, // холст

    surfaceBright = secondary30, //поверхность Яркая
    surfaceContainer = secondary30,
    surfaceContainerHigh = secondary30, // поверхность Контейнер Высокий
    surfaceContainerHighest = secondary30, // поверхность Контейнер Высший
    surfaceContainerLow = secondary30, // поверхность Контейнер Низкий
    surfaceContainerLowest = secondary30, // поверхность Контейнер Самый низкий
    surfaceDim = secondary30, // поверхность Тусклый
)

private val DarkColorScheme = darkColorScheme(
    // buttons
    primary = primary60,
    onPrimary = primary100,

    primaryContainer = primary30,
    onPrimaryContainer = primary90,

    secondary = secondary20,
    onSecondary = secondary100,

    // карточка курса, поверхность
    secondaryContainer = cardBackgroundDark,
    onSecondaryContainer = cardTextDark,

    // кнопка google
    tertiary = secondary40,
    onTertiary = secondary30,

    // для border Настроить напоминание
    tertiaryContainer = tertiaryBorderBlue,
    onTertiaryContainer = tertiary90,

    error = error80,
    onError = error20,

    errorContainer = error30,
    onErrorContainer = error90,

    outline = neutralVariant60,
    outlineVariant = neutralVariant60,

    surface = primary20,
    onSurface = primary100,

    // AlertDialog
    surfaceVariant = primary40,
    onSurfaceVariant = primary100,

    background = primary01,
    onBackground = secondary90,
)
*/
/*
internal object DialogTokens {
    val ActionFocusLabelTextColor = ColorSchemeKeyTokens.Primary
    val ActionHoverLabelTextColor = ColorSchemeKeyTokens.Primary
    val ActionLabelTextColor = ColorSchemeKeyTokens.Primary
    val ActionLabelTextFont = TypographyKeyTokens.LabelLarge
    val ActionPressedLabelTextColor = ColorSchemeKeyTokens.Primary
    val ContainerColor = ColorSchemeKeyTokens.Surface // containerColor
    val ContainerElevation = ElevationTokens.Level3
    val ContainerShape = ShapeKeyTokens.CornerExtraLarge
    val ContainerSurfaceTintLayerColor = ColorSchemeKeyTokens.SurfaceTint
    val HeadlineColor = ColorSchemeKeyTokens.OnSurface // titleContentColor
    val HeadlineFont = TypographyKeyTokens.HeadlineSmall
    val SupportingTextColor = ColorSchemeKeyTokens.OnSurfaceVariant // textContentColor
    val SupportingTextFont = TypographyKeyTokens.BodyMedium
    val IconColor = ColorSchemeKeyTokens.Secondary // iconContentColor
    val IconSize = 24.0.dp
}
 */

@Composable
fun MyBADTheme(
    darkThemeInit: Boolean? = null,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme: Boolean = darkThemeInit ?: isSystemInDarkTheme()

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
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).run {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
