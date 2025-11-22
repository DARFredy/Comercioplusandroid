package com.example.comercioplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = Orange,
    background = DarkBlue,
    surface = CardBlue,
    onPrimary = White,          // Texto sobre botones primarios
    onBackground = White,       // Texto principal sobre el fondo
    onSurface = White,          // Texto principal sobre las tarjetas
    onSurfaceVariant = TextGrey // Texto secundario/apagado
)

@Composable
fun ComercioplusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
