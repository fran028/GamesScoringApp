package com.example.games_scoring_app.Games

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.PlayerWithScores
import com.example.games_scoring_app.Data.ScoreTypes
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow

@Composable
fun GeneralaScoreboard(
    playersWithScores: List<PlayerWithScores>,
    scoreTypes: List<ScoreTypes>,
    themeMode: Int,
    onAddScore: (Scores) -> Unit,
    onUpdateScore: (Scores) -> Unit
) {
    val TAG = "GeneralaScoreboard"

    // These are the UI labels for the rows, which should match the order from the database
    val opcionesGenerala = scoreTypes.map { it.name }

    // This local data defines the sequence of possible scores for each category
    val valoresGenerala: List<List<String>> = listOf(
        listOf("-", "1", "2", "3", "4", "5", "x"),
        listOf("-", "2", "4", "6", "8", "12", "x"),
        listOf("-", "3", "6", "9", "12", "15", "x"),
        listOf("-", "4", "8", "12", "16", "20", "x"),
        listOf("-", "5", "10", "15", "20", "25", "x"),
        listOf("-", "6", "12", "18", "24", "30", "x"),
        listOf("-", "20", "25", "x"),
        listOf("-", "30", "35", "x"),
        listOf("-", "40", "45", "x"),
        listOf("-", "50", "55", "x"),
        listOf("-", "100", "105", "x")
    )

    // Calculate total scores directly from the playerWithScores object
    val playerTotals = playersWithScores.map { player ->
        player.scores.sumOf { it.score }
    }

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

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
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Column for Score Type Labels (PLAYERS, 1, 2, 3...)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(90.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(45.dp)
                        .background(buttonColor, shape = RoundedCornerShape(7.5.dp))
                        .padding(2.5.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "PLAYERS",
                        fontFamily = LeagueGothic,
                        fontSize = 24.sp,
                        color = buttonFontColor,
                        modifier = Modifier.padding(end = 4.dp),
                        textAlign = TextAlign.Right,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                // Create a row label for each score type
                for (valor in opcionesGenerala) {
                    val fontSize = 24.sp
                    Box(
                        modifier = Modifier
                            .width(90.dp)
                            .height(45.dp)
                            .background(blue, shape = RoundedCornerShape(7.5.dp))
                            .padding(2.5.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = valor,
                            fontFamily = LeagueGothic,
                            fontSize = fontSize,
                            color = black,
                            modifier = Modifier.padding(end = 4.dp),
                            textAlign = TextAlign.Right,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                // TOTAL row label
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(45.dp)
                        .background(buttonColor, shape = RoundedCornerShape(7.5.dp))
                        .padding(2.5.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "TOTAL",
                        fontFamily = LeagueGothic,
                        fontSize = 24.sp,
                        color = buttonFontColor,
                        modifier = Modifier.padding(end = 4.dp),
                        textAlign = TextAlign.Right,
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            // Columns for each player's scores
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(272.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    playersWithScores.forEachIndexed { playerIndex, playerWithScores ->
                        val player = playerWithScores.player
                        val minWidth = 30.dp
                        val maxWidth = (272 / playersWithScores.size - 4.5 * playersWithScores.size).dp
                        val width = if (maxWidth < minWidth) minWidth else maxWidth
                        val playerName = if (playersWithScores.size > 2) player.name.take(2) else player.name

                        // A column for each player
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(width),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            // Player Name Box
                            Box(
                                modifier = Modifier
                                    .width(width)
                                    .height(45.dp)
                                    .background(blue, shape = RoundedCornerShape(7.5.dp))
                                    .padding(2.5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = playerName,
                                    fontFamily = LeagueGothic,
                                    fontSize = 24.sp,
                                    color = black,
                                    textAlign = TextAlign.Center,
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            // Score cells for this player
                            scoreTypes.forEachIndexed { scoreTypeIndex, scoreType ->
                                // Find the existing score from the database for this player and this category
                                val existingScore = playerWithScores.scores.find { it.id_score_type == scoreType.id }
                                val currentScoreValue = existingScore?.score ?: 0
                                val scoreOptions = valoresGenerala.getOrElse(scoreTypeIndex) { listOf("-", "0", "x") }

                                // Find the index of the current score in the options list to cycle through it
                                val currentOptionIndex = scoreOptions.indexOf(currentScoreValue.toString()).coerceAtLeast(0)

                                val displayedValue = when (existingScore?.score) {
                                    null -> "-" // No score entry exists yet
                                    0 -> "-"    // Score is 0, treat as unfilled
                                    -1 -> "x"   // Score is -1, it's a crossed-out 'tacha'
                                    else -> existingScore.score.toString() // Display the actual score number
                                }

                                val boxBackgroundColor = when {
                                    displayedValue == "x" -> backgroundColor
                                    displayedValue == "-" -> gray
                                    scoreTypeIndex >= 6 && currentScoreValue == scoreOptions.getOrNull(2)?.toIntOrNull() -> yellow
                                    else -> buttonColor
                                }
                                val textColor = when {
                                    displayedValue == "x" -> fontColor
                                    displayedValue == "-" -> white
                                    scoreTypeIndex >= 6 && currentScoreValue == scoreOptions.getOrNull(2)?.toIntOrNull() -> black
                                    else -> buttonFontColor
                                }

                                Box(
                                    modifier = Modifier
                                        .width(width)
                                        .height(45.dp)
                                        .background(boxBackgroundColor, shape = RoundedCornerShape(7.5.dp))
                                        .padding(0.dp)
                                        .clickable {
                                            // --- DATABASE LOGIC ---
                                            val nextOptionIndex = (currentOptionIndex + 1) % scoreOptions.size
                                            val nextValueStr = scoreOptions[nextOptionIndex]
                                            val nextScoreInt = when (nextValueStr) {
                                                "x" -> -1 // Use -1 to represent 'x' in the database
                                                "-" -> 0
                                                else -> nextValueStr.toIntOrNull() ?: 0
                                            }

                                            if (existingScore != null) {
                                                // Score exists, update it
                                                val updatedScore = existingScore.copy(score = nextScoreInt)
                                                onUpdateScore(updatedScore)
                                                Log.d(TAG, "Updating score for ${player.name}, ${scoreType.name}: $nextScoreInt")
                                            } else {
                                                // No score exists, create a new one
                                                val newScore = Scores(
                                                    id_player = player.id,
                                                    id_score_type = scoreType.id,
                                                    score = nextScoreInt,
                                                    isFinalScore = false // Or determine this based on game rules
                                                )
                                                onAddScore(newScore)
                                                Log.d(TAG, "Adding score for ${player.name}, ${scoreType.name}: $nextScoreInt")
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = displayedValue,
                                        fontFamily = LeagueGothic,
                                        fontSize = 24.sp,
                                        color = textColor,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            // Total Box
                            Box(
                                modifier = Modifier
                                    .width(width)
                                    .height(45.dp)
                                    .background(buttonColor, shape = RoundedCornerShape(7.5.dp))
                                    .padding(2.5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = playerTotals.getOrElse(playerIndex) { 0 }.toString(),
                                    fontFamily = LeagueGothic,
                                    fontSize = 24.sp,
                                    color = buttonFontColor,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(4.5.dp))
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
}
