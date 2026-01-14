package com.example.games_scoring_app.Games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.PlayerWithScores
import com.example.games_scoring_app.Data.ScoreTypes
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white


@Composable
fun LevelsScoreboard(
    playersWithScores: List<PlayerWithScores>,
    scoreTypes: List<ScoreTypes>,
    themeMode: Int,
    onAddScore: (Scores) -> Unit,
    onUpdateScore: (Scores) -> Unit
) {
    val TAG = "LevelsScoreboard"

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

    // Find the relevant ScoreType for "Levels", which is usually one "Final Score".
    val finalScoreType = scoreTypes.find { it.name == "Final Score" }

    if (playersWithScores.isEmpty() || finalScoreType == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(backgroundColor)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LOADING DATA...",
                fontFamily = LeagueGothic,
                fontSize = 48.sp,
                color = fontColor,
                modifier = Modifier
            )
        }
    } else {
        val numPlayers = playersWithScores.size
        val minwidth = 40.dp
        val maxwidth = (372 / playersWithScores.size - 6.4 * (playersWithScores.size-1)).dp
        val width = if (maxwidth < minwidth) minwidth else maxwidth

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .width(372.dp)
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                playersWithScores.forEach { playerWithScores ->
                    val player = playerWithScores.player
                    val playerNameDisplay = if (numPlayers > 3 && player.name.length > 3) player.name.substring(0, 3) else player.name

                    // The player's level is stored in the 'score' property of their first and only Scores object.
                    val levelScore = playerWithScores.scores.firstOrNull()
                    val currentLevel = levelScore?.score ?: 0

                    PlayerLevelColumn(
                        playerName = playerNameDisplay,
                        currentLevel = currentLevel,
                        width = width,
                        onLevelUp = {
                            val newLevel = currentLevel + 1
                            if (levelScore != null) {
                                // If a score entry exists, update it
                                onUpdateScore(levelScore.copy(score = newLevel))
                            } else {
                                // If no score entry exists, create one
                                val newScore = Scores(
                                    id_player = player.id,
                                    id_score_type = finalScoreType.id,
                                    score = newLevel,
                                    isFinalScore = true
                                )
                                onAddScore(newScore)
                            }
                        },
                        onLevelDown = {
                            if (currentLevel > 0) {
                                val newLevel = currentLevel - 1
                                if (levelScore != null) {
                                    // Update the existing score entry
                                    onUpdateScore(levelScore.copy(score = newLevel))
                                }
                                // No need for an 'else' here, as you can't level down from 0
                            }
                        },
                        buttonColor = buttonColor,
                        buttonFontColor = buttonFontColor
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerLevelColumn(
    playerName: String,
    width: Dp,
    currentLevel: Int,
    onLevelUp: () -> Unit,
    onLevelDown: () -> Unit,
    buttonColor: Color,
    buttonFontColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(width)
            .padding(0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(
                    blue,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = playerName,
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = black,
                textAlign = TextAlign.Center, // Changed for better alignment
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(
                    red,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp)
                .clickable { onLevelDown() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                fontFamily = LeagueGothic,
                fontSize = 32.sp,
                color = black,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        for (i in 0 until currentLevel) {
            val level = i + 1
            Box(
                modifier = Modifier
                    .width(width)
                    .height(45.dp)
                    .background(
                        buttonColor,
                        shape = RoundedCornerShape(7.5.dp)
                    )
                    .padding(2.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if( level == currentLevel) level.toString() else "",
                    fontFamily = LeagueGothic,
                    fontSize = 32.sp,
                    color = buttonFontColor,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(green, shape = RoundedCornerShape(7.5.dp))
                .padding(2.5.dp)
                .clickable { onLevelUp() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = black,
                textAlign = TextAlign.Center,
            )
        }
    }
}
