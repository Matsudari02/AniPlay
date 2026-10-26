package com.example.aniplay.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AniOrange = Color(0xFFF47521)
val AniBackground = Color(0xFF0B0B0F)
val AniSurface = Color(0xFF16161D)
val AniSurfaceVariant = Color(0xFF25252F)
val AniOnSurface = Color(0xFFEDEDF0)
val AniOnSurfaceVariant = Color(0xFFA6A6B2)

private val AniDarkColors = darkColorScheme(
    primary = AniOrange,
    onPrimary = Color.Black,
    secondary = AniOrange,
    background = AniBackground,
    onBackground = AniOnSurface,
    surface = AniSurface,
    onSurface = AniOnSurface,
    surfaceVariant = AniSurfaceVariant,
    onSurfaceVariant = AniOnSurfaceVariant,
    surfaceContainer = AniSurface,
    surfaceContainerHigh = AniSurfaceVariant,
)

/** O app é sempre escuro, como o streaming original. */
@Composable
fun AniPlayTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AniDarkColors,
        content = content,
    )
}
