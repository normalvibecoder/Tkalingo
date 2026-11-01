package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TkaLingoColorScheme = lightColorScheme(
    primary = DuoFeatherGreen,
    onPrimary = Color.White,
    primaryContainer = DuoFeatherGreenLight,
    onPrimaryContainer = Color(0xFF1F4D00),
    secondary = DuoSkyBlue,
    onSecondary = Color.White,
    secondaryContainer = DuoSkyBlueLight,
    onSecondaryContainer = Color(0xFF00486B),
    tertiary = DuoSunGold,
    onTertiary = DuoInkDark,
    tertiaryContainer = DuoSunGoldLight,
    onTertiaryContainer = Color(0xFF5A4000),
    error = DuoHeartRed,
    onError = Color.White,
    errorContainer = DuoHeartRedLight,
    onErrorContainer = Color(0xFF680005),
    background = DuoWarmBg,
    onBackground = DuoInkDark,
    surface = DuoCardSurface,
    onSurface = DuoInkDark,
    surfaceVariant = Color(0xFFEEF3F8),
    onSurfaceVariant = DuoInkMuted,
    outline = DuoBorderGray
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TkaLingoColorScheme,
        typography = Typography,
        content = content
    )
}
