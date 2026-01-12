package com.example.games_scoring_app.Utilities

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
// Import FlowRow
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Components.PlayerAmountGrid
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.cream
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
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
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.height(3 * dotSize)
                ) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.height(3 * dotSize)
                ) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
            }
            5 -> Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.size(3 * dotSize)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
                DiceDot(dotSize)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
            }
            6 -> Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(3 * dotSize)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.height(5 * dotSize)
                ) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.height(5 * dotSize)
                ) {
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                    DiceDot(dotSize)
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RollDice(){
    val TAG = "RollDicePage"
    Log.d(TAG, "RollDicePage called")

    val maxDiceCount = 8
    val minDiceCount = 1

    var selectedDiceCount by remember { mutableStateOf(1) }
    var diceValues by remember { mutableStateOf(List(maxDiceCount) { 0 }) }
    var rollDice by remember { mutableStateOf(false) }
    // --- NEW: State to track which dice are locked ---
    val lockedDice = remember { mutableStateListOf<Boolean>().apply { addAll(List(maxDiceCount) { false }) } }

    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp.dp }

    val dicePerRow = 4
    val diceSpacing = 16.dp
    val diceSize = remember(screenWidth) {
        val totalPadding = 32.dp
        val totalSpacing = (dicePerRow - 1) * diceSpacing
        (screenWidth - totalPadding - totalSpacing) / dicePerRow
    }
    val diceCorner = remember {
        (diceSize / 4)
    }
    Log.d(TAG, "diceSize: $diceSize")


    Text(
        text = "DICE TO ROLL",
        fontFamily = LeagueGothic,
        fontSize = 48.sp,
        color = white,
        modifier = Modifier
            .fillMaxWidth() ,
        textAlign = TextAlign.Left
    )
    Spacer(modifier = Modifier.height(20.dp))
    PlayerAmountGrid(
        maxPlayers = maxDiceCount,
        minPlayers = minDiceCount,
        selectedAmount = selectedDiceCount,
        onPlayerAmountSelected = { amount ->
            selectedDiceCount = amount
            // When the count changes, reset all locks
            for (i in 0 until maxDiceCount) {
                lockedDice[i] = false
            }
        },
        modifier = Modifier
            .fillMaxWidth() ,
        selectedbgcolor = blue,
        bgcolor = darkgray,
        textcolor = white,
    )
    Spacer(modifier = Modifier.height(20.dp))
    // --- NEW: A Row to hold the buttons ---
    Row(
        modifier = Modifier
            .fillMaxWidth() ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val buttonWidth = (screenWidth-32.dp)/2-diceSpacing/2
        ButtonBar(
            onClick = {
                rollDice = true
            },
            text = "ROLL DICE",
            bgcolor = blue,
            height = 64.dp,
            textcolor = white,
            width = buttonWidth
        )
        // --- NEW: Unlock All button ---
        ButtonBar(
            onClick = {
                for (i in 0 until maxDiceCount) {
                    lockedDice[i] = false
                }
            },
            text = "UNLOCK ALL",
            bgcolor = yellow,
            height = 64.dp,
            textcolor = darkgray,
            width = buttonWidth
        )
    }
    LaunchedEffect(rollDice) {
        if (rollDice) {
            val animationFrames = 10
            for (i in 0 until animationFrames) {
                // --- UPDATE: Only roll dice that are not locked ---
                diceValues = List(maxDiceCount) { index ->
                    if (index < selectedDiceCount && !lockedDice[index]) (1..6).random() else diceValues[index]
                }
                delay(50)
            }
            // --- UPDATE: Final roll also respects the lock ---
            diceValues = List(maxDiceCount) { index ->
                if (index < selectedDiceCount && !lockedDice[index]) (1..6).random() else diceValues[index]
            }
            rollDice = false
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    FlowRow (
        modifier = Modifier
            .fillMaxWidth() ,
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
                    .width(diceSize)
                    .height(diceSize)
                    .background(backgroundColor, shape = RoundedCornerShape(diceCorner))
//                    .border(
//                        width = if (isLocked) 3.dp else 0.dp,
//                        color = if (isLocked) yellow else Color.Transparent,
//                        shape = RoundedCornerShape(diceCorner)
//                    )
                    .pointerInput(Unit) {
                        detectTapGestures {
                            if (isSelected && value > 0) { // Can only lock active, rolled dice
                                lockedDice[i] = !lockedDice[i]
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected && value > 0) {
                    DiceFace(value = value, diceSize = diceSize)
                }
            }
        }
    }
}
