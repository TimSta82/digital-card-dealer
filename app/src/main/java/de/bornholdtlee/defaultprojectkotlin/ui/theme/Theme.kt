package de.bornholdtlee.defaultprojectkotlin.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@SuppressLint("ConflictingOnColor")
private val DarkColors = darkColors(
    primary = Gunmetal,
    primaryVariant = MetallicSeaweed,
    secondary = MetallicSeaweed,
    surface = Gunmetal,
    onSurface = White,
    background = RichBlack
)

private val LightColors = lightColors(
    primary = EggBlue,
    primaryVariant = CastletonGreen,
    secondary = CastletonGreen,
    surface = DogwoodRose,
    onSurface = White,
    background = White
)

private val Shapes = Shapes(
    small = RoundedCornerShape(one_GU),
    medium = RoundedCornerShape(two_GU),
    large = RoundedCornerShape(four_GU)
)

@Composable
fun DefaultTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {

    MaterialTheme(
        colors = if (useDarkTheme) DarkColors else LightColors,
        shapes = Shapes,
        content = content
    )
}
