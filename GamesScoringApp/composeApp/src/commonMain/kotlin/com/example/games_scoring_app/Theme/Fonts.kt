package com.example.games_scoring_app.Theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import gamesscoringapp.composeapp.generated.resources.Res
import gamesscoringapp.composeapp.generated.resources.leaguegothic
import gamesscoringapp.composeapp.generated.resources.robotocondensed
import gamesscoringapp.composeapp.generated.resources.robotomono

// GoogleFont provider removed as it is Android specific and apparently unused or replaced by local fonts.

val LeagueGothic: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.leaguegothic, FontWeight.Normal)
    )

val RobotoCondensed: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.robotocondensed, FontWeight.Normal)
    )
val RobotoMono: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.robotomono, FontWeight.Normal)
    )