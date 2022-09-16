package dev.atick.core.ui.theme

import ai.atick.material.MaterialColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = PrimaryVariant,
    primaryVariant = Primary,
    secondary = PrimaryVariant,
    secondaryVariant = Primary,
    background = Color(0xFF_202430),
    surface = Color(0xFF_2E3440),
    error = MaterialColor.Red700,
    onPrimary = MaterialColor.White,
    onSecondary = MaterialColor.White,
    onBackground = MaterialColor.BlueGray50,
    onSurface = MaterialColor.BlueGray50,
    onError = MaterialColor.White
)

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Primary,
    secondaryVariant = PrimaryVariant,
    background = MaterialColor.BlueGray50,
    surface = MaterialColor.White,
    error = MaterialColor.Red700,
    onPrimary = MaterialColor.White,
    onSecondary = MaterialColor.White,
    onBackground = MaterialColor.BlueGray800,
    onSurface = MaterialColor.BlueGray800,
    onError = MaterialColor.White
)

@Composable
fun ComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content,
    )
}