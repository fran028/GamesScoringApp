package com.example.games_scoring_app.Utilities

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.*
import gamesscoringapp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun PlayerPicker() {
    var playerName by remember { mutableStateOf("") }
    val players = remember { mutableStateListOf<String>() }
    var selectedPlayer by remember { mutableStateOf<Pair<String, Color>?>(null) }
    var isSpinning by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableStateOf(0f) }
    val haptic = LocalHapticFeedback.current

    val scope = rememberCoroutineScope()

    val availableColors = listOf(blue, green, yellow, red)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Randomly select a random player from the list",
            fontSize = 16.sp,
            fontFamily = RobotoCondensed,
            color = gray
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = {
                    Text(
                        "PLAYER NAME",
                        fontFamily = RobotoCondensed,
                        fontWeight = FontWeight.Bold,
                        color = white
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
                    .border(
                        BorderStroke(4.dp, blue),
                        shape = RoundedCornerShape(10.dp)
                    ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp), // Matches typical "SetUp" style
                textStyle = TextStyle(
                    fontFamily = LeagueGothic,
                    fontSize = 28.sp,
                    color = white
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = gray.copy(alpha = 0.3f),
                    focusedContainerColor = gray.copy(alpha = 0.5f),
                    focusedIndicatorColor = blue,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = blue,
                    focusedLabelColor = blue,
                    unfocusedLabelColor = blue
                ),
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(64.dp) // Matches standard TextField height
                    .background(blue, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        if (playerName.isNotBlank()) {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            players.add(playerName)
                            playerName = ""
                        }
                    }
                ) {
                    Text(
                        text = "+",
                        fontSize = 32.sp,
                        fontFamily = LeagueGothic,
                        color = darkgray // Text color updated to darkgray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (players.isNotEmpty()) {
            Box(contentAlignment = Alignment.TopCenter) {
                // The Spinning Wheel
                Canvas(modifier = Modifier
                    .size(250.dp)
                    .rotate(rotationAngle)
                ) {
                    val sliceAngle = 360f / players.size
                    players.forEachIndexed { index, _ ->
                        drawArc(
                            color = availableColors[index % availableColors.size],
                            startAngle = index * sliceAngle,
                            sweepAngle = sliceAngle,
                            useCenter = true,
                            size = Size(size.width, size.height)
                        )
                    }
                }

                // WHITE SELECTION LINE (Marker at the top)
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                        .height(46.dp)
                        .background(black, RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = green,
                    contentColor = darkgray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = !isSpinning && players.size > 1,
                onClick = {
                    scope.launch {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        isSpinning = true
                        val winnerIndex = Random.nextInt(players.size)
                        val sliceAngle = 360f / players.size
                        // 270 degrees is the top of the wheel in Compose Canvas
                        val targetRotation = (360f * 5) + (270f - (winnerIndex * sliceAngle) - (sliceAngle / 2))

                        // --- NEW: Add state to track the last passed segment ---
                        var lastPassedSegment by mutableStateOf(-1)

                        animate(
                            initialValue = rotationAngle % 360f,
                            targetValue = targetRotation,
                            animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
                        ) { value, _ ->
                            rotationAngle = value

                            // --- NEW: Haptic feedback logic ---
                            val currentSegment = ((value + (sliceAngle / 2) - 270f) / sliceAngle).toInt()
                            if (currentSegment != lastPassedSegment) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                lastPassedSegment = currentSegment
                            }
                            // --- End of new logic ---
                        }

                        selectedPlayer = players[winnerIndex] to availableColors[winnerIndex % availableColors.size]
                        isSpinning = false
                    }
                }

            ) {
                Text( text = if (players.size < 2) "Add more players" else "Spin the Wheel!",
                    fontFamily = RobotoCondensed,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Result Display (Transparent background, Colored text)
        selectedPlayer?.let { (name, color) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = name,
                        fontSize = 32.sp, // Made slightly larger for impact
                        color = color,
                        fontFamily = RobotoCondensed
                    )
                }
            }
        }

        // DIVIDER LINE on top of player list
        if (players.isNotEmpty()) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = gray.copy(alpha = 0.5f)
            )
        }

        players.forEachIndexed { index, player ->
            val playerColor = availableColors[index % availableColors.size]

            ListItem(
                headlineContent = { Text(
                    text = player,
                    fontFamily = RobotoCondensed,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold) },
                leadingContent = {
                    Box(modifier = Modifier.size(48.dp).background(playerColor, RoundedCornerShape(8.dp)))
                },
                trailingContent = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(red, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = {

                            haptic.performHapticFeedback(HapticFeedbackType.Reject)
                            if (selectedPlayer?.first == player) selectedPlayer = null
                            players.remove(player)
                        }) {
                            Icon(
                                painter = painterResource(Res.drawable.trash),
                                contentDescription = "Delete",
                                tint = darkgray, // Icon color updated to darkgray
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}