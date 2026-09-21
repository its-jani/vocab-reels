package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = DeepBackground,
    primaryContainer = ElectricIndigo,
    onPrimaryContainer = LightText,
    secondary = VibrantPurple,
    onSecondary = LightText,
    tertiary = CoralPink,
    onTertiary = LightText,
    background = DeepBackground,
    onBackground = LightText,
    surface = DeepSurface,
    onSurface = LightText,
    surfaceVariant = DeepSurfaceVariant,
    onSurfaceVariant = SubtitleText,
    outline = CardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricIndigo,
    onPrimary = LightText,
    primaryContainer = DeepSurfaceVariant,
    onPrimaryContainer = LightText,
    secondary = VibrantPurple,
    onSecondary = LightText,
    tertiary = CoralPink,
    onTertiary = LightText,
    background = DeepBackground,
    onBackground = LightText,
    surface = DeepSurface,
    onSurface = LightText,
    surfaceVariant = DeepSurfaceVariant,
    onSurfaceVariant = SubtitleText,
    outline = CardBorder
)

@Composable
fun VocabReelsTheme(
    darkTheme: Boolean = true, // TikTok/Reels inspired immersive dark UI by default
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepBackground.toArgb()
            window.navigationBarColor = DeepBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    VocabReelsTheme(darkTheme = darkTheme, content = content)
}
