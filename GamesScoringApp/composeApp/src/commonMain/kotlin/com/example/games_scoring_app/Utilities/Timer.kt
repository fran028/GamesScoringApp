package com.example.games_scoring_app.Utilities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.*
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun Timer() {
    // Total duration selected by the user in seconds
    var totalTime by remember { mutableStateOf(60L) }
    // Time remaining in seconds, starts equal to total time
    var timeLeft by remember { mutableStateOf(totalTime) }
    // Tracks if the timer is currently running
    var isRunning by remember { mutableStateOf(false) }

    // This effect runs the countdown logic
    LaunchedEffect(key1 = timeLeft, key2 = isRunning) {
        if (isRunning && timeLeft > 0) {
            delay(1000L) // Wait for 1 second
            timeLeft--
        } else if (timeLeft == 0L) {
            isRunning = false // Stop the timer when it reaches zero
        }
    }

    // List of predefined time options in seconds
    val timeOptions = listOf(10L, 30L, 60L, 120L)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- TIME SELECTION ---
        Text(
            text = "SELECT TIME (SECONDS)",
            fontFamily = LeagueGothic,
            fontSize = 32.sp,
            color = white
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Box container for the time options ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkgray, shape = RoundedCornerShape(10.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                timeOptions.forEach { time ->
                    Button(
                        modifier = Modifier.padding(0.dp),
                        onClick = {
                            if (!isRunning) { // Allow changing time only when timer is stopped
                                totalTime = time
                                timeLeft = time
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (totalTime == time) blue else gray, // Use gray for better contrast
                            contentColor = if (totalTime == time) black else white
                        ),
                        enabled = !isRunning
                    ) {
                        Text(
                            text = time.toString(),
                            fontFamily = LeagueGothic,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(darkgray, shape = RoundedCornerShape(10.dp))
                .padding(24.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Background circle
                drawArc(
                    color = black, // Use gray for better contrast
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 30f, cap = StrokeCap.Round)
                )
                // Progress circle
                drawArc(
                    color = blue,
                    startAngle = -90f,
                    sweepAngle = 360f * (timeLeft.toFloat() / totalTime.toFloat()),
                    useCenter = false,
                    style = Stroke(width = 30f, cap = StrokeCap.Round)
                )
            }
            // Text showing time left
            Text(
                text = "${(timeLeft / 60).toString().padStart(2, '0')}:${(timeLeft % 60).toString().padStart(2, '0')}",
                fontFamily = LeagueGothic,
                fontSize = 80.sp,
                color = white
            )
        }

        // Spacer to push buttons to the bottom
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))
        // --- CONTROL BUTTONS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Start/Pause Button
            Button(
                onClick = { isRunning = !isRunning },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) yellow else green,
                    contentColor = white
                ),
                modifier = Modifier
                    .weight(2f)
                    .height(60.dp)
            ) {
                Text(
                    text = if (isRunning) "PAUSE" else "START",
                    fontFamily = RobotoCondensed,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            // Reset Button
            Button(
                onClick = {
                    isRunning = false
                    timeLeft = totalTime
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = red,
                    contentColor = white
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                Text(
                    text = "RESET",
                    fontFamily = RobotoCondensed,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
