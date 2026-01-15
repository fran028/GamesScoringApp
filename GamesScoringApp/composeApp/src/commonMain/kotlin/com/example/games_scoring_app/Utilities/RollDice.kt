package com.example.games_scoring_app.Utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
// KMP Navigation and Resources

import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.PlayerAmountGrid
import com.example.games_scoring_app.Theme.*
import gamesscoringapp.composeapp.generated.resources.Res
import gamesscoringapp.composeapp.generated.resources.lock
import kotlinx.coroutines.delay

@Composable
private fun DiceDot(dotSize: Dp) {
    Box(
        modifier = Modifier
            .size(dotSize)
            .clip(CircleShape)
            .background(black)
    )
}

@Composable
private fun DiceFace(value: Int, diceSize: Dp) {
    val dotSize = diceSize / 5
    Box(
        modifier = Modifier
            .size(diceSize)
            .padding(dotSize / 2),
        contentAlignment = Alignment.Center
    ) {
        when (value) {
            1 -> DiceDot(dotSize)
            2 -> Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.size(3 * dotSize)
            ) {
                DiceDot(dotSize)
                DiceDot(dotSize)
            }
            3 -> Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.size(5 * dotSize)
            ) {
                DiceDot(dotSize)
                DiceDot(dotSize)
                DiceDot(dotSize)
            }
            4 -> Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(3 * dotSize)
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(3 * dotSize)) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
                Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(3 * dotSize)) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
            }
            5 -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.size(3 * dotSize)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
                DiceDot(dotSize)
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
            }
            6 -> Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(3 * dotSize)
            ) {
                Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(5 * dotSize)) {
                    DiceDot(dotSize); DiceDot(dotSize); DiceDot(dotSize)
                }
                Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(5 * dotSize)) {
                    DiceDot(dotSize); DiceDot(dotSize); DiceDot(dotSize)
                }
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RollDice() {
    val maxDiceCount = 8
    val minDiceCount = 1

    var selectedDiceCount by remember { mutableStateOf(1) }
    var diceValues by remember { mutableStateOf(List(maxDiceCount) { 0 }) }
    var rollDice by remember { mutableStateOf(false) }
    val lockedDice = remember { mutableStateListOf<Boolean>().apply { addAll(List(maxDiceCount) { false }) } }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val screenWidth = maxWidth
        val dicePerRow = 4
        val diceSpacing = 16.dp
        val diceSize = (screenWidth - (diceSpacing * (dicePerRow - 1))) / dicePerRow
        val diceCorner = diceSize / 4

        Column {
            Text(
                text = "DICE TO ROLL",
                fontFamily = LeagueGothic,
                fontSize = 48.sp,
                color = white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            // Added instruction text
            Text(
                text = "Tap a die to lock/unlock it",
                fontFamily = LeagueGothic, // Assuming same font family
                fontSize = 18.sp,
                color = cream, // Using a lighter color for subtitle
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            PlayerAmountGrid(
                maxPlayers = maxDiceCount,
                minPlayers = minDiceCount,
                selectedAmount = selectedDiceCount,
                onPlayerAmountSelected = { amount ->
                    selectedDiceCount = amount
                    for (i in 0 until maxDiceCount) { lockedDice[i] = false }
                },
                modifier = Modifier.fillMaxWidth(),
                selectedbgcolor = blue,
                bgcolor = darkgray,
                textcolor = white,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val buttonWidth = (screenWidth / 2) - (diceSpacing / 2)
                ButtonBar(
                    onClick = { rollDice = true },
                    text = "ROLL DICE",
                    bgcolor = blue,
                    height = 64.dp,
                    textcolor = white,
                    width = buttonWidth
                )
                ButtonBar(
                    onClick = { for (i in 0 until maxDiceCount) { lockedDice[i] = false } },
                    text = "UNLOCK ALL",
                    bgcolor = yellow,
                    height = 64.dp,
                    textcolor = darkgray,
                    width = buttonWidth
                )
            }

            LaunchedEffect(rollDice) {
                if (rollDice) {
                    repeat(10) {
                        diceValues = List(maxDiceCount) { index ->
                            if (index < selectedDiceCount && !lockedDice[index]) (1..6).random() else diceValues[index]
                        }
                        delay(50)
                    }
                    rollDice = false
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(diceSpacing),
                verticalArrangement = Arrangement.spacedBy(diceSpacing)
            ) {
                for (i in 0 until maxDiceCount) {
                    val isSelected = i < selectedDiceCount
                    val isLocked = lockedDice[i]
                    val backgroundColor = if (isSelected) if (isLocked) yellow else cream else darkgray
                    val value = diceValues[i]

                    Box(
                        modifier = Modifier
                            .size(diceSize)
                            .background(backgroundColor, shape = RoundedCornerShape(diceCorner))
                            .pointerInput(isSelected, value) { // Re-bind when selection or value changes
                                detectTapGestures {
                                    // Requirement: Only allow locking if the die is selected AND has been rolled (value > 0)
                                    if (isSelected && value > 0) {
                                        lockedDice[i] = !lockedDice[i]
                                    }
                                }
                            }
                    ) {
                        // 1. Draw the dots ONLY if there is a value
                        if (isSelected && value > 0) {
                            DiceFace(value = value, diceSize = diceSize)
                        }

                        // 2. Draw the lock icon ONLY if locked
                        // We move this OUTSIDE the "value > 0" UI block to ensure
                        // it can display correctly once the state changes.
                        if (isSelected && isLocked) {
                            androidx.compose.foundation.Image(
                                painter = org.jetbrains.compose.resources.painterResource(Res.drawable.lock),
                                contentDescription = "Locked",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(diceSize / 4)
                            )
                        }
                    }
                }
            }
        }
    }
}