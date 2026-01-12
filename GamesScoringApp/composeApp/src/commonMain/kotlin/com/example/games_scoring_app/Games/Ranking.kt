package com.example.games_scoring_app.Games

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.games_scoring_app.Theme.copper
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.silver
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow


@Composable
fun RankingScoreboard(
    playersWithScores: List<PlayerWithScores>,
    scoreTypes: List<ScoreTypes>,
    themeMode: Int,
    onAddScore: (Scores) -> Unit,
    onUpdateScore: (Scores) -> Unit
) {
    val TAG = "RankingScoreboard"
    Log.d(TAG, "RankingScoreboard called")

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

    // Find the relevant ScoreType for "Ranking", which is usually one "Final Score".
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
                color = fontColor
            )
        }
    } else {
        val numPlayers = playersWithScores.size
        val minWidth = 40.dp
        val maxWidth = (372 / numPlayers - 6.4 * (numPlayers - 1)).dp
        val width = if (maxWidth < minWidth) minWidth else maxWidth

        var selectedPlayerIndex by remember { mutableStateOf(0) }

        // State to hold the mapping of rank -> player's original index
        val rankingAssignments = remember { mutableStateOf(List(numPlayers) { -1 }) }

        // Effect to initialize ranking from the database scores
        LaunchedEffect(playersWithScores) {
            val initialAssignments = MutableList(numPlayers) { -1 }
            playersWithScores.forEachIndexed { playerIndex, playerWithScores ->
                val rank = playerWithScores.scores.find { it.id_score_type == finalScoreType.id }?.score
                if (rank != null && rank in 1..numPlayers) {
                    val rankListIndex = rank - 1 // A rank of 1 maps to index 0
                    if (initialAssignments[rankListIndex] == -1) { // Avoid overwriting if data is weird
                        initialAssignments[rankListIndex] = playerIndex
                    }
                }
            }
            rankingAssignments.value = initialAssignments
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .width(372.dp)
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier.padding(0.dp).fillMaxWidth(),
                    text = "Select from Player List",
                    fontFamily = LeagueGothic,
                    fontSize = 32.sp,
                    color = fontColor,
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    playersWithScores.forEachIndexed { index, playerWithScore ->
                        val player = playerWithScore.player
                        val playerNameDisplay = if (numPlayers > 2 && player.name.length > 2) player.name.substring(0, 2) else player.name
                        PlayerRankingColumn(
                            playerName = playerNameDisplay,
                            columnWidth = width,
                            playerIndex = index,
                            isSelected = selectedPlayerIndex == index,
                            onPlayerClicked = { clickedPlayerIndex ->
                                selectedPlayerIndex = clickedPlayerIndex
                            },
                            buttonColor = buttonColor,
                            buttonFontColor = buttonFontColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(0.dp).fillMaxWidth(),
                    text = "Players Ranking",
                    fontFamily = LeagueGothic,
                    fontSize = 32.sp,
                    color = fontColor,
                    textAlign = TextAlign.Left,
                )
                val prefixList = listOf("st", "nd", "rd", "th")
                for (rankIndex in 0 until numPlayers) {
                    val currentRank = rankIndex + 1 // 1 for 1st, 2 for 2nd...
                    val assignedPlayerOriginalIndex = rankingAssignments.value[rankIndex]
                    val displayPlayerName = if (assignedPlayerOriginalIndex != -1) {
                        playersWithScores.getOrNull(assignedPlayerOriginalIndex)?.player?.name ?: "ERROR"
                    } else {
                        "Tap to assign"
                    }
                    val rankText = if (currentRank > 3) "$currentRank${prefixList[3]}" else "$currentRank${prefixList[currentRank - 1]}"

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .clickable(enabled = selectedPlayerIndex != -1) {
                                val playerToAssign = playersWithScores[selectedPlayerIndex]
                                val existingScore = playerToAssign.scores.find { it.id_score_type == finalScoreType.id }

                                // --- DATABASE LOGIC ---
                                if (existingScore != null) {
                                    // Player already has a rank, update it
                                    val updatedScore = existingScore.copy(score = currentRank)
                                    onUpdateScore(updatedScore)
                                } else {
                                    // Player has no rank yet, create it
                                    val newScore = Scores(
                                        id_player = playerToAssign.player.id,
                                        score = currentRank,
                                        id_score_type = finalScoreType.id,
                                        isFinalScore = true
                                    )
                                    onAddScore(newScore)
                                }
                                Log.d(TAG, "Assigned player ${playerToAssign.player.name} to rank $currentRank")

                                // Update local UI state for immediate feedback
                                val newAssignments = rankingAssignments.value.toMutableList()
                                // Clear old assignment for this player
                                val oldIndex = newAssignments.indexOf(selectedPlayerIndex)
                                if (oldIndex != -1) newAssignments[oldIndex] = -1
                                // Set new assignment
                                newAssignments[rankIndex] = selectedPlayerIndex
                                rankingAssignments.value = newAssignments
                            },
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(46.dp)
                                .height(45.dp)
                                .background(backgroundColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = rankText,
                                fontFamily = LeagueGothic,
                                fontSize = 36.sp,
                                color = fontColor,
                                textAlign = TextAlign.Center,
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f) // Takes remaining width
                                .height(45.dp)
                                .background(
                                    when (currentRank) {
                                        1 -> yellow
                                        2 -> silver
                                        3 -> copper
                                        numPlayers -> red
                                        else -> buttonColor
                                    },
                                    shape = RoundedCornerShape(7.5.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 2.5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayPlayerName.uppercase(),
                                fontFamily = LeagueGothic,
                                fontSize = 24.sp,
                                color = if (currentRank in 1..3 || currentRank == numPlayers) black else buttonFontColor,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PlayerRankingColumn(
    playerName: String,
    columnWidth: Dp,
    playerIndex: Int,
    isSelected: Boolean,
    onPlayerClicked: (Int) -> Unit,
    buttonColor: Color,
    buttonFontColor: Color

) {
    Box(
        modifier = Modifier
            .width(columnWidth)
            .height(45.dp)
            .background(
                if (isSelected) blue else buttonColor,
                shape = RoundedCornerShape(7.5.dp)
            )
            .padding(2.5.dp)
            .clickable {
                onPlayerClicked(playerIndex)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = playerName,
            fontFamily = LeagueGothic,
            fontSize = 24.sp,
            color = if (isSelected) black else buttonFontColor,
            textAlign = TextAlign.Center,
        )
    }
    Spacer(modifier = Modifier.height(6.dp))
}
