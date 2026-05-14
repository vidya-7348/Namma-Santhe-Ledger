package com.example.nammasantheledger.frontend.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryTealLight,
    onPrimaryContainer = TextOnPrimary,
    secondary = SecondaryCoral,
    onSecondary = TextOnPrimary,
    secondaryContainer = SecondaryCoralLight,
    onSecondaryContainer = TextPrimary,
    tertiary = TertiarySand,
    onTertiary = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundGray,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed,
    onError = TextOnPrimary,
    background = BackgroundGray,
    onBackground = TextPrimary,
    outline = DividerGray
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTealLight,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryTeal,
    onPrimaryContainer = TextOnPrimary,
    secondary = SecondaryCoralLight,
    onSecondary = TextPrimary,
    secondaryContainer = SecondaryCoral,
    onSecondaryContainer = TextOnPrimary,
    tertiary = TertiarySand,
    onTertiary = TextPrimary,
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFB0B0B0),
    error = ErrorRedLight,
    onError = TextOnPrimary,
    background = Color(0xFF121212),
    onBackground = Color(0xFFF5F5F5),
    outline = Color(0xFF404040)
)

@Composable
fun NammaSantheLedgerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
