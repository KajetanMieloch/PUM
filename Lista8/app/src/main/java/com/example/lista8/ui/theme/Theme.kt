package com.example.lista8.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    secondary = Yellow
)

private val LightColorScheme = lightColorScheme(
    primary = Purple,
    secondary = Yellow
)

@Composable
fun Lista8Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
