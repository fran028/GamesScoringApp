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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.games_scoring_app.Theme.yellow

@Composable
fun PuntosScoreboard(
    playersWithScores: List<PlayerWithScores>,
    scoreTypes: List<ScoreTypes>,
    maxScore: Int,
    themeMode: Int,
    onAddScore: (Scores) -> Unit,
    onUpdateScore: (Scores) -> Unit,
    // --- NEW: Add lambda for deleting a score ---
    onDeleteScore: (Scores) -> Unit
) {
    val TAG = "PuntosScoreboard"

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

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
        val minWidth = 40.dp
        val maxWidth = (372 / playersWithScores.size - 6.4 * (playersWithScores.size - 1)).dp
        val width = if (maxWidth < minWidth) minWidth else maxWidth

        var showAddPopup by remember { mutableStateOf(false) }
        var showEditPopup by remember { mutableStateOf(false) }
        var selectedPlayer by remember { mutableStateOf<PlayerWithScores?>(null) }
        var selectedScore by remember { mutableStateOf<Scores?>(null) }
        var inputValue by remember { mutableStateOf("") }

        // --- Popup for Adding a new score ---
        if (showAddPopup && selectedPlayer != null) {
            ScorePopup(
                isEditMode = false,
                onDismiss = { showAddPopup = false },
                onConfirm = {
                    val scoreValue = inputValue.toIntOrNull()
                    if (scoreValue != null) {
                        val newScore = Scores(
                            id_player = selectedPlayer!!.player.id,
                            id_score_type = finalScoreType.id,
                            score = scoreValue,
                            isFinalScore = false
                        )
                        onAddScore(newScore)
                    }
                    showAddPopup = false
                    inputValue = ""
                },
                onDelete = {}, // Not used in add mode
                inputValue = inputValue,
                onInputValueChange = { inputValue = it },
                playername = selectedPlayer!!.player.name,
                buttonColor = buttonColor,
                buttonFontColor = buttonFontColor
            )
        }

        // --- NEW: Popup for Editing/Deleting an existing score ---
        if (showEditPopup && selectedPlayer != null && selectedScore != null) {
            ScorePopup(
                isEditMode = true,
                onDismiss = { showEditPopup = false },
                onConfirm = {
                    val scoreValue = inputValue.toIntOrNull()
                    if (scoreValue != null) {
                        // Create a copy of the selected score with the updated value
                        val updatedScore = selectedScore!!.copy(score = scoreValue)
                        onUpdateScore(updatedScore)
                    }
                    showEditPopup = false
                    inputValue = ""
                },
                onDelete = {
                    onDeleteScore(selectedScore!!)
                    showEditPopup = false
                    inputValue = ""
                },
                inputValue = inputValue,
                onInputValueChange = { inputValue = it },
                playername = selectedPlayer!!.player.name,
                buttonColor = buttonColor,
                buttonFontColor = buttonFontColor
            )
        }

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
                    val playerName = if (playersWithScores.size > 2) playerWithScores.player.name.take(2) else playerWithScores.player.name
                    PlayerPuntosColumn(
                        playerName = playerName,
                        scores = playerWithScores.scores,
                        maxScore = maxScore,
                        width = width,
                        onAddScoreClicked = {
                            selectedPlayer = playerWithScores
                            showAddPopup = true
                        },
                        // --- NEW: Handle score clicks ---
                        onScoreClicked = { score ->
                            selectedPlayer = playerWithScores
                            selectedScore = score
                            inputValue = score.score.toString() // Pre-fill the input
                            showEditPopup = true
                        },
                        buttonColor = buttonColor,
                        buttonFontColor = buttonFontColor,
                        fontColor = fontColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PlayerPuntosColumn(
    playerName: String,
    scores: List<Scores>,
    maxScore: Int,
    width: Dp,
    onAddScoreClicked: () -> Unit,
    // --- NEW: Lambda to handle score clicks ---
    onScoreClicked: (Scores) -> Unit,
    buttonColor: Color,
    buttonFontColor: Color,
    fontColor: Color
) {
    val totalScore = scores.sumOf { it.score }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .height(500.dp)
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
                    if (maxScore > 0 && totalScore >= maxScore) red else buttonColor,
                    shape = RoundedCornerShape(7.5.dp)
                )
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = playerName,
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = buttonFontColor,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Column(modifier = Modifier.weight(1f).verticalScroll(scrollState)) {
            scores.forEach { score ->
                Text(
                    text = score.score.toString(),
                    fontFamily = LeagueGothic,
                    fontSize = 36.sp,
                    color = fontColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        // --- NEW: Make each score clickable ---
                        .clickable { onScoreClicked(score) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = totalScore.toString(),
            fontFamily = LeagueGothic,
            fontSize = 36.sp,
            color = if (maxScore > 0 && totalScore >= maxScore) red else green,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(width)
                .height(45.dp)
                .background(buttonColor, shape = RoundedCornerShape(7.5.dp))
                .padding(2.5.dp)
                .clickable { onAddScoreClicked() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = buttonFontColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// --- RENAMED and MODIFIED to handle both Add and Edit/Delete modes ---
@Composable
private fun ScorePopup(
    isEditMode: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDelete: () -> Unit,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    playername: String,
    buttonColor: Color,
    buttonFontColor: Color
) {
    val title = if (isEditMode) "Edit Score for $playername" else "Add Score to $playername"
    val confirmText = if (isEditMode) "Update" else "Add"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        containerColor = buttonColor,
        textContentColor = buttonFontColor,
        titleContentColor = buttonFontColor,
        text = {
            OutlinedTextField(
                value = inputValue,
                onValueChange = onInputValueChange,
                label = { Text("Enter score") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = buttonColor,
                    focusedContainerColor = buttonColor,
                    focusedIndicatorColor = buttonFontColor,
                    disabledContainerColor = buttonColor,
                    focusedTextColor = buttonFontColor,
                    unfocusedTextColor = buttonFontColor,
                    cursorColor = buttonFontColor,
                ),
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = blue)
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            Row {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = yellow)
                ) {
                    Text(text = "Cancel")
                }
                // --- NEW: Show Delete button only in edit mode ---
                if (isEditMode) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = red)
                    ) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    )
}
