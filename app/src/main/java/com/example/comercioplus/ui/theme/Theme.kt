package com.example.comercioplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Paleta para el modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = White,
    onBackground = White,
    onSurface = White, // Texto principal sobre fondos y superficies oscuras
    onSurfaceVariant = TextGrey // Texto secundario
)

// Paleta para el modo claro
private val LightColorScheme = lightColorScheme(
    primary = Orange,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = White,
    onBackground = TextPrimaryLight, // Texto principal sobre fondos y superficies claras
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight // Texto secundario
)

@Composable
fun ComercioplusTheme(
    darkTheme: Boolean = true, // Por defecto, el tema oscuro está activado
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
