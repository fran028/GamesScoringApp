package com.example.games_scoring_app.Utilities

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.RobotoMono
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun RandomNumberGenerator() {
    val haptic = LocalHapticFeedback.current

    var minInput by remember { mutableStateOf("") }
    var maxInput by remember { mutableStateOf("") }
    var warningMessage by remember { mutableStateOf<String?>(null) }

    val limits = 1000000
    val maxDigits = limits.toString().length

    var currentResult by remember { mutableStateOf<Int?>(null) }

    // Initialize with zeros (padding to the limit length)
    var animatedDisplay by remember { mutableStateOf("0".repeat(maxDigits)) }

    val history = remember { mutableStateListOf<Int>() }

    // Animation Logic: Right-to-Left replacement
    LaunchedEffect(currentResult) {
        currentResult?.let { result ->
            // Convert result to string and pad with zeros to match maxDigits
            val targetString = result.toString().padStart(maxDigits, '0')

            // We iterate from the last index (right) to the first index (left)
            for (i in maxDigits - 1 downTo 0) {
                val currentChars = animatedDisplay.toCharArray()
                currentChars[i] = targetString[i]
                // Fix: Use concatToString() instead of the String constructor
                animatedDisplay = currentChars.concatToString()
                delay(80) // Adjust speed of digit change here
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Generate random numbers (-$limits to $limits)",
            fontSize = 16.sp,
            fontFamily = RobotoCondensed,
            color = gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = minInput,
                onValueChange = { input ->
                    if (input.all { char -> char.isDigit() || char == '-' } || input.isEmpty()) {
                        val num = input.toIntOrNull()
                        if (num != null) {
                            if (num > limits) {
                                minInput = limits.toString()
                                warningMessage = "Max limit is $limits"
                            } else if (num < -limits) {
                                minInput = (-limits).toString()
                                warningMessage = "Min limit is -$limits"
                            } else {
                                minInput = input
                                warningMessage = null
                            }
                        } else {
                            minInput = input
                            warningMessage = null
                        }
                    }
                },
                label = { Text("MIN", fontFamily = RobotoCondensed, color = white) },
                modifier = Modifier.weight(1f).height(64.dp).border(
                    BorderStroke(4.dp, blue),
                    shape = RoundedCornerShape(10.dp)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontFamily = LeagueGothic, fontSize = 32.sp, color = white),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = black,
                    focusedContainerColor = black,
                    focusedIndicatorColor = blue,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = blue
                ),
            )
            TextField(
                value = maxInput,
                onValueChange = { input ->
                    if (input.all { char -> char.isDigit() || char == '-' } || input.isEmpty()) {
                        val num = input.toIntOrNull()
                        if (num != null) {
                            if (num > limits) {
                                maxInput = limits.toString()
                                warningMessage = "Max limit is $limits"
                            } else if (num < -limits) {
                                maxInput = (-limits).toString()
                                warningMessage = "Min limit is -$limits"
                            } else {
                                maxInput = input
                                warningMessage = null
                            }
                        } else {
                            maxInput = input
                            warningMessage = null
                        }
                    }
                },
                label = { Text("MAX", fontFamily = RobotoCondensed, color = white) },
                modifier = Modifier.weight(1f).height(64.dp).border(
                    BorderStroke(4.dp, blue),
                    shape = RoundedCornerShape(10.dp)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontFamily = LeagueGothic, fontSize = 32.sp, color = white),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = black,
                    focusedContainerColor = black,
                    focusedIndicatorColor = blue,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = blue
                ),
            )
        }

        warningMessage?.let { message ->
            Text(
                text = message,
                color = red,
                fontFamily = RobotoCondensed,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val min = minInput.toIntOrNull() ?: 0
                val max = maxInput.toIntOrNull() ?: 0

                if (max >= min) {
                    val nextNum = Random.nextInt(min, max + 1)
                    currentResult = nextNum
                    history.add(0, nextNum)
                    warningMessage = null
                } else {
                    warningMessage = "Min cannot be greater than Max"
                }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = green, contentColor = darkgray),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("GENERATE", fontFamily = LeagueGothic, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display Result with padded zeros and Right-to-Left animation
        Text(
            text = animatedDisplay,
            fontSize = 120.sp,
            fontFamily = LeagueGothic,
            color = white,
            softWrap = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Number List", fontSize = 24.sp, fontFamily = RobotoCondensed, color = white)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = gray)

        val columns = 3
        val historyChunks = history.chunked(columns)

        historyChunks.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { number ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = darkgray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = number.toString(),
                                fontSize = 18.sp,
                                fontFamily = LeagueGothic,
                                color = white,
                                maxLines = 1
                            )
                        }
                    }
                }
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}