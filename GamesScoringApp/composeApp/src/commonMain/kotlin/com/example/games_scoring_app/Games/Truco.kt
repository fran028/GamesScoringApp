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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Data.PlayerWithScores
import com.example.games_scoring_app.Data.ScoreTypes
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow

@Composable
fun TrucoScoreboard(
    playersWithScores: List<PlayerWithScores>,
    scoreTypes: List<ScoreTypes>,
    maxScore: Int,
    themeMode: Int,
    onAddScore: (Scores) -> Unit,
    onUpdateScore: (Scores) -> Unit
) {
    val TAG = "Truco"

    // Find the relevant ScoreType for Truco, which is usually just one "Final Score".
    val finalScoreType = scoreTypes.find { it.name == "Final Score" }

    // Safely get the players. Defaults to "P1" and "P2" if data is not ready.
    val player1 = playersWithScores.getOrNull(0)
    val player2 = playersWithScores.getOrNull(1)

    // Find the actual score object for each player from the database.
    val score1 = player1?.scores?.find { it.id_score_type == finalScoreType?.id }
    val score2 = player2?.scores?.find { it.id_score_type == finalScoreType?.id }

    val backgroundColor = if (themeMode == 0) black else white
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black

    if (player1 == null || player2 == null || finalScoreType == null) {
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
                    .padding(vertical = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Player 1 Column
                PlayerTrucoColumn(
                    playerName = player1.player.name,
                    // Provide the score value, or 0 if it doesn't exist yet.
                    score = score1?.score ?: 0,
                    maxScore = maxScore,
                    onScoreClick = { newScoreValue ->
                        // The user clicked a score line. We need to update the database.
                        if (score1 != null) {
                            // If the score object exists, create an updated copy and send it to the ViewModel.
                            val updatedScore = score1.copy(score = newScoreValue)
                            onUpdateScore(updatedScore)
                        } else {
                            // If the score object doesn't exist, create a new one and send it.
                            val newScore = Scores(
                                id_player = player1.player.id,
                                id_score_type = finalScoreType.id,
                                score = newScoreValue,
                                isFinalScore = true
                            )
                            onAddScore(newScore)
                        }
                    },
                    buttonColor = buttonColor,
                    buttonFontColor = buttonFontColor,
                    fontColor = fontColor
                )

                // Center Divider
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Offset: Player Name Box (45.dp) + Initial Spacer (16.dp)
                    //Spacer(modifier = Modifier.height(45.dp + 16.dp))

                    val nameboxHeight = 45+8
                    val boxesPerHalf = (maxScore / 5) / 2
                    val boxGroupHeight = 9 + 80 + 9 + 16

                    // Define the spacing you want between the divider parts
                    val innerSpacing = 10.dp

                    // 2. Top vertical line (Malas)
                    // Subtract innerSpacing to keep the horizontal line centered in the gap
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .height(((boxGroupHeight * boxesPerHalf) + nameboxHeight - innerSpacing.value).dp)
                            .background(buttonColor, shape = RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.height(innerSpacing))

                    // 4. The Middle Horizontal Line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(buttonColor, shape = RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.height(innerSpacing))
                    // 6. Bottom vertical line (Buenas)
                    Box(
                        modifier = Modifier
                            .width(10.dp)
                            .height(((boxGroupHeight * boxesPerHalf) - innerSpacing.value).dp)
                            .background(buttonColor, shape = RoundedCornerShape(5.dp))
                    )
                }




                // Player 2 Column
                PlayerTrucoColumn(
                    playerName = player2.player.name,
                    score = score2?.score ?: 0,
                    maxScore = maxScore,
                    onScoreClick = { newScoreValue ->
                        // Same logic as Player 1
                        if (score2 != null) {
                            val updatedScore = score2.copy(score = newScoreValue)
                            onUpdateScore(updatedScore)
                        } else {
                            val newScore = Scores(
                                id_player = player2.player.id,
                                id_score_type = finalScoreType.id,
                                score = newScoreValue,
                                isFinalScore = true
                            )
                            onAddScore(newScore)
                        }
                    },
                    buttonColor = buttonColor,
                    buttonFontColor = buttonFontColor,
                    fontColor = fontColor
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PlayerTrucoColumn(
    playerName: String,
    score: Int,
    maxScore: Int,
    onScoreClick: (Int) -> Unit,
    buttonColor: Color,
    buttonFontColor: Color,
    fontColor: Color
) {
    val TAG = "Truco"
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(150.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val lineLength = 80.dp
        val lineThick = 9.dp
        val diagonalLineLength = 80.dp
        val rowWidth = 100.dp

        val colorBuenas = yellow
        val colorMalas = buttonColor
        val colorNormal = gray
        Box(
            modifier = Modifier
                .width(125.dp)
                .height(45.dp)
                .background(
                    if /*(score >= maxScore) yellow else if */(score >= maxScore / 2) colorBuenas else buttonColor,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = playerName,
                fontFamily = LeagueGothic,
                fontSize = 32.sp,
                color = if (score >= maxScore || score >= maxScore / 2) black else buttonFontColor,
                textAlign = TextAlign.Right,
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        for (i in 0 until maxScore / 5) {
            Box(
                modifier = Modifier
                    .width(lineLength)
                    .height(lineThick)
                    .padding(0.dp)
                    .background(
                        if ((i * 5) + 1 <= score) if ((i * 5) + 1 > maxScore / 2) colorBuenas else colorMalas else colorNormal,
                        shape = RoundedCornerShape(2.5.dp)
                    )
                    .clickable { onScoreClick(1 + (5 * i)) },
            )
            Row(
                modifier = Modifier
                    .width(rowWidth)
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(lineThick)
                        .height(lineLength)
                        .padding(0.dp)
                        .background(
                            if ((i * 5) + 2 <= score) if ((i * 5) + 2 > maxScore / 2) colorBuenas else colorMalas else colorNormal,
                            shape = RoundedCornerShape(2.5.dp)
                        )
                        .clickable { onScoreClick(2 + 5 * i) },
                )
                Box(
                    modifier = Modifier
                        .width(lineThick)
                        .height(diagonalLineLength)
                        .rotate(45f)
                        .padding(0.dp)
                        .background(
                            if ((i * 5) + 3 <= score) if ((i * 5) + 3 > maxScore / 2) colorBuenas else colorMalas else colorNormal,
                            shape = RoundedCornerShape(2.5.dp)
                        )
                        .clickable { onScoreClick(3 + 5 * i) },
                )
                Box(
                    modifier = Modifier
                        .width(lineThick)
                        .height(lineLength)
                        .padding(0.dp)
                        .background(
                            if ((i * 5) + 4 <= score) if ((i * 5) + 4 > maxScore / 2) colorBuenas else colorMalas else colorNormal,
                            shape = RoundedCornerShape(2.5.dp)
                        )
                        .clickable { onScoreClick(4 + 5 * i) },
                )
            }
            Box(
                modifier = Modifier
                    .width(lineLength)
                    .height(lineThick)
                    .padding(0.dp)
                    .background(
                        if ((i * 5) + 5 <= score) if ((i * 5) + 5 > maxScore / 2) colorBuenas else colorMalas else colorNormal,
                        shape = RoundedCornerShape(2.5.dp)
                    )
                    .clickable { onScoreClick(5 + 5 * i) },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
