package com.example.games_scoring_app.Theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.games_scoring_app.ui.theme.Pink40
import com.example.games_scoring_app.ui.theme.Pink80
import com.example.games_scoring_app.ui.theme.Purple40
import com.example.games_scoring_app.ui.theme.Purple80
import com.example.games_scoring_app.ui.theme.PurpleGrey40
import com.example.games_scoring_app.ui.theme.PurpleGrey80

// IMPORTANT: Import your specific Typography variable from the correct package
// This prevents the conflict with kotlin.text.Typography
import com.example.games_scoring_app.ui.theme.Typography

// Assuming these colors are defined in your Color.kt
// If Color.kt is in the same package (com.example.games_scoring_app.Theme),
// they don't need imports.
/*
import com.example.games_scoring_app.Theme.Purple80
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.white
... etc
*/

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = black,
    surface = black,
    onPrimary = white,
    onSecondary = white,
    onTertiary = white,
    onBackground = white,
    onSurface = white
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = white,
    surface = white,
    onPrimary = black,
    onSecondary = black,
    onTertiary = black,
    onBackground = black,
    onSurface = black
)

@Composable
fun Games_Scoring_AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Removed Android-only Dynamic Color logic for KMP compatibility
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // This now correctly refers to your val in Type.kt
        content = content
    )
}
