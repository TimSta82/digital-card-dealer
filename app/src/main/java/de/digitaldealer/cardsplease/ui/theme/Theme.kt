package de.digitaldealer.cardsplease.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val Shapes = Shapes(
    small = RoundedCornerShape(one_GU),
    medium = RoundedCornerShape(two_GU),
    large = RoundedCornerShape(four_GU)
)
//
//private val MainColorsLight = lightColors(
//    primary = lightPrimary,
//    primaryVariant = lightPrimaryVariant,
//    secondary = lightAccent,
//    secondaryVariant = dialogColor,
//    background = lightPrimaryDark,
//    surface = lightPrimaryDark,
//    error = lightAlert
////    onPrimary = ,
////    onSecondary = ,
////    onBackground = ,
////    onSurface = ,
////    onError = ,
////    onSurface = White,
//)
//
//private val MainColorsDark = darkColors(
//    primary = darkPrimary,
//    primaryVariant = darkPrimaryVariant,
//    secondary = lightAccent,
//    surface = darkPrimaryDark,
//    error = lightAlert,
////    onSurface = White,
//    background = darkPrimaryDark,
//    secondaryVariant = dialogColor
//)

@SuppressLint("ConflictingOnColor")
private val DarkColors = darkColors(
//    primary = Night_Primary,
//    primaryVariant = Night_Primary_Variant,
    secondary = Night_Secondary,
    secondaryVariant = Night_Secondary_Variant,
    onSecondary = Night_OnSecondary,
    surface = Night_Surface,
    onSurface = Night_On_Surface,
    background = Night_Background,
    onBackground = Night_OnBackground,
    error = Night_Error
)

@SuppressLint("ConflictingOnColor")
private val LightColors = lightColors(
//    primary = Light_Primary,
//    primaryVariant = Light_Primary_Variant,
    secondary = Light_Secondary,
    secondaryVariant = Light_Secondary_Variant,
    onSecondary = Light_OnSecondary,
    surface = Light_Surface,
    onSurface = Light_On_Surface,
    background = Light_Background,
    onBackground = Light_OnBackground,
    error = Light_Error
)

@Composable
fun MainTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = if (useDarkTheme) DarkColors else LightColors,
        shapes = Shapes,
        content = content
    )
}
