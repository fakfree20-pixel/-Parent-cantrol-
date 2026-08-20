package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NaturalGreen400,
    onPrimary = NaturalGreen900,
    primaryContainer = NaturalGreen800,
    onPrimaryContainer = NaturalGreen100,
    secondary = MossGreen500,
    onSecondary = Color(0xFF1B221A),
    secondaryContainer = MossGreen700,
    onSecondaryContainer = MossGreen100,
    tertiary = EarthAmber500,
    onTertiary = Color(0xFF2A1800),
    tertiaryContainer = EarthAmber700,
    onTertiaryContainer = EarthAmber100,
    background = Color(0xFF121511),
    onBackground = Color(0xFFE1E4DC),
    surface = Color(0xFF191D17),
    onSurface = Color(0xFFE1E4DC),
    surfaceVariant = Color(0xFF2B3128),
    onSurfaceVariant = Color(0xFFC2C9BD),
    outline = Color(0xFF8C9388),
    error = Terracotta600,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = NaturalGreen700, // #386633 Organic Sage Green
    onPrimary = Color.White,
    primaryContainer = NaturalGreen100, // #D1E9CF
    onPrimaryContainer = NaturalGreen900, // #121F0E
    secondary = NaturalTextSecondary, // #43493F
    onSecondary = Color.White,
    secondaryContainer = NaturalCardBg, // #E0E9DB
    onSecondaryContainer = NaturalTextPrimary,
    tertiary = EarthAmber600,
    onTertiary = Color.White,
    tertiaryContainer = EarthAmber100,
    onTertiaryContainer = EarthAmber700,
    background = NaturalBg, // #FBFDF8
    onBackground = NaturalTextPrimary, // #1A1C18
    surface = NaturalSurface, // #FFFFFF
    onSurface = NaturalTextPrimary, // #1A1C18
    surfaceVariant = NaturalSurfaceVariant, // #ECF3E8
    onSurfaceVariant = NaturalTextSecondary, // #43493F
    outline = NaturalBorder, // #DDE5D9
    error = Terracotta700, // #BA1A1A
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
