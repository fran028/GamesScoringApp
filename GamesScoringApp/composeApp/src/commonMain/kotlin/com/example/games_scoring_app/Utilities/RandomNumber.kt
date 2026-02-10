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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white
import kotlin.random.Random

@Composable
fun RandomNumberGenerator() {
    // State for input strings
    var minInput by remember { mutableStateOf("") }
    var maxInput by remember { mutableStateOf("") }

    // State for the warning message
    var warningMessage by remember { mutableStateOf<String?>(null) }

    val limits = 100000

    // State for the current generated number
    var currentResult by remember { mutableStateOf<Int?>(null) }

    // State for the session history
    val history = remember { mutableStateListOf<Int>() }

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

        // Range Inputs
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

        // Warning Message
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

        // Generate Button
        Button(
            onClick = {
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

        // Display Result
        Text(
            text = currentResult?.toString() ?: "000000",
            fontSize = 64.sp,
            fontFamily = RobotoCondensed,
            color = white
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Number List", fontSize = 24.sp, fontFamily = RobotoCondensed, color = white)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = gray)

        // Using chunks to create a grid effect (3 columns)
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
                            .weight(1f) // Ensures all boxes are the same width
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
                                // Adjusted font size to ensure 6 digits fit comfortably
                                fontSize = 18.sp,
                                fontFamily = LeagueGothic,
                                color = white,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Add empty spacers if the last row isn't full to keep alignment
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}