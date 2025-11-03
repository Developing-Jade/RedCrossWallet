// src/commonMain/kotlin/org/example/redcrosswalletapp/theme/LightTheme.kt
package org.example.redcrosswalletapp.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ---------------------------------------------------------------
// Light color palette – everything is based on BrandRed
// ---------------------------------------------------------------
private val LightColors: ColorScheme = lightColorScheme(
    primary = BrandRed,               // main brand colour (buttons, app bar, etc.)
    onPrimary = Color.White,          // text/icons on primary
    onSecondary = Color.White,
    background = Color(0xFFF3FFC5),   // soft‑green background
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFB00020),        // default error color
    onError = Color.White
)

/**
 * Light‑only theme wrapper.
 *
 * @param content Your app’s UI tree.
 */
@Composable
fun RedCrossWalletLightTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        // Supply custom Typography / Shapes here
        content = content
    )
}