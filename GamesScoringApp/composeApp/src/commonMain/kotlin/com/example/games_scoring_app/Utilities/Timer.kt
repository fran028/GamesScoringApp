package com.example.games_scoring_app.Utilities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import gamesscoringapp.composeapp.generated.resources.Res
import gamesscoringapp.composeapp.generated.resources.*

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun Timer() {

    val haptic = LocalHapticFeedback.current
    var totalTime by remember { mutableStateOf(60L) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }

    // New state for Dialog
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = timeLeft, key2 = isRunning) {
        if (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
            if (timeLeft in 11..totalTime) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }
            if (timeLeft in 1..10) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        } else if (timeLeft == 0L) {
            isRunning = false
        }
    }

    LaunchedEffect(timeLeft, isRunning) {
        // Only fire if the timer actually reached 0 (and isn't just initialized at 0)
        if (timeLeft == 0L && !isRunning && totalTime > 0) {
            repeat(3) {
                repeat(25) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    delay(50L)
                }
                delay(100)
            }
        }
    }

    if (showTimePicker) {
        var hInput by remember { mutableStateOf((totalTime / 3600).toString().padStart(2, '0')) }
        var mInput by remember { mutableStateOf(((totalTime % 3600) / 60).toString().padStart(2, '0')) }
        var sInput by remember { mutableStateOf((totalTime % 60).toString().padStart(2, '0')) }

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            containerColor = darkgray,
            title = { Text("SET DURATION", fontFamily = LeagueGothic, color = white, fontSize = 32.sp) },
            text = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimeInputBuffer("HH", hInput) { if (it.length <= 2) hInput = it }
                    Text(":", color = white, fontSize = 32.sp)
                    TimeInputBuffer("MM", mInput) { if (it.length <= 2) mInput = it }
                    Text(":", color = white, fontSize = 32.sp)
                    TimeInputBuffer("SS", sInput) { if (it.length <= 2) sInput = it }
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = blue),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                        val h = hInput.toLongOrNull() ?: 0L
                        val m = mInput.toLongOrNull() ?: 0L
                        val s = sInput.toLongOrNull() ?: 0L
                        totalTime = (h * 3600) + (m * 60) + s
                        timeLeft = totalTime
                        isRunning = false
                        showTimePicker = false
                    }
                ) {
                    Text("SET", fontFamily = LeagueGothic, fontSize = 24.sp)
                }
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkgray, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp, vertical = 5.dp)
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showTimePicker = true }, // Opens popup
                contentAlignment = Alignment.Center
            ) {
                val hours = timeLeft / 3600
                val minutes = (timeLeft % 3600) / 60
                val seconds = timeLeft % 60
                val timeString = "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"


                Text(
                    text = timeString,
                    fontFamily = LeagueGothic,
                    fontSize = 90.sp,
                    color = white,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // --- 2. MIDDLE SECTION (HOURGLASS + BUTTONS) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp), // FIXED HEIGHT prevents overlapping title bar
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hourglass
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(darkgray, shape = RoundedCornerShape(10.dp))
                    .padding(20.dp)
            ) {
                val progress = timeLeft.toFloat() / totalTime.toFloat()
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val centerX = w / 2
                    val centerY = h / 2
                    val neckWidthHalf = 24f

                    // 1. Define the Frame Path first (to use for both clipping or reference)
                    val framePath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(0f, 0f)
                        lineTo(w, 0f)
                        lineTo(centerX + neckWidthHalf, centerY)
                        lineTo(w, h)
                        lineTo(0f, h)
                        lineTo(centerX - neckWidthHalf, centerY)
                        close()
                    }

                    // 2. DRAW SAND FIRST (So it appears behind the border)
                    val fillPercentage = 1f - progress

                    // Bottom Sand
                    if (fillPercentage > 0) {
                        val sandLevelY = h - ((h / 2) * fillPercentage)
                        val currentWidthHalf =
                            androidx.compose.ui.util.lerp(w / 2, neckWidthHalf, fillPercentage)
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(centerX - currentWidthHalf, sandLevelY)
                            lineTo(centerX + currentWidthHalf, sandLevelY)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(
                            path,
                            color = yellow,
                        )

                    }

                    // Top Sand
                    if (progress > 0) {
                        val sandLevelY = (h / 2) * (1f - progress)
                        val currentWidthHalf =
                            androidx.compose.ui.util.lerp(neckWidthHalf, w / 2, progress)
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(centerX - currentWidthHalf, sandLevelY)
                            lineTo(centerX + currentWidthHalf, sandLevelY)
                            lineTo(centerX + neckWidthHalf, centerY)
                            lineTo(centerX - neckWidthHalf, centerY)
                            close()
                        }
                        drawPath(
                            path,
                            color = yellow,
                        )
                    }

                    // Falling Stream (Dotted/Circles)
                    if (isRunning && timeLeft > 0) {
                        val bottomSandLevelY = h - ((h / 2) * (1f - progress))
                        drawLine(
                            color = yellow,
                            start = Offset(centerX - neckWidthHalf / 2, centerY),
                            end = Offset(centerX - neckWidthHalf / 2, h),
                            strokeWidth = neckWidthHalf, // Fills the neck width
                            cap = androidx.compose.ui.graphics.StrokeCap.Round, // Makes the dots circular
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                // 0f dash length with Round cap creates a circle
                                // 40f is the spacing between the centers of the circles
                                intervals = floatArrayOf(0f, neckWidthHalf * 2),
                                phase = 0f
                            )
                        )
                        drawLine(
                            color = yellow,
                            start = Offset(
                                centerX + neckWidthHalf / 2,
                                centerY + neckWidthHalf
                            ),
                            end = Offset(centerX + neckWidthHalf / 2, h - neckWidthHalf),
                            strokeWidth = neckWidthHalf, // Fills the neck width
                            cap = androidx.compose.ui.graphics.StrokeCap.Round, // Makes the dots circular
                            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                // 0f dash length with Round cap creates a circle
                                // 40f is the spacing between the centers of the circles
                                intervals = floatArrayOf(0f, neckWidthHalf * 2),
                                phase = 0f
                            )
                        )
                    }

                    // 3. DRAW FRAME LAST (With Rounded Corners)
                    drawPath(
                        path = framePath,
                        color = white,
                        style = Stroke(
                            width = 16f,
                            pathEffect = androidx.compose.ui.graphics.PathEffect.cornerPathEffect(
                                20f
                            ) // Increased for more noticeable rounding
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.width(16.dp))

            // Buttons
            Column(
                modifier = Modifier.width(120.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showTimePicker = true },
                    modifier = Modifier.size(100.dp)
                        .background(yellow, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.sand_clock),
                        contentDescription = "Timer",
                        tint = black,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isRunning = !isRunning },
                    modifier = Modifier.size(100.dp)
                        .background(if (isRunning) yellow else green, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        painter = painterResource(if (isRunning) Res.drawable.pause else Res.drawable.play),
                        contentDescription = "Toggle",
                        tint = if (isRunning) black else white,
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        isRunning = false; timeLeft = totalTime},
                        modifier = Modifier.size(100.dp).background(red, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.undo),
                        contentDescription = "Reset",
                        tint = white,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        // Increase both to keep the hourglass visual consistent
                        totalTime += 10
                        timeLeft += 10
                    },
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blue,
                        contentColor = white
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "+10s", fontFamily = LeagueGothic, fontSize = 32.sp, color = white)
                }
            }
        }
    }
}


@Composable
fun TimeInputBuffer(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = gray, fontSize = 12.sp)
        TextField(
            value = value,
            onValueChange = {
                // Only allow update if the new string is all digits
                if (it.all { char -> char.isDigit() }) {
                    onValueChange(it)
                }
            },
            modifier = Modifier.width(60.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = white,
                fontSize = 24.sp,
                fontFamily = LeagueGothic,
                textAlign = TextAlign.Center
            ),
            // Opens the numeric keypad
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = gray,
                unfocusedContainerColor = darkgray,
                focusedIndicatorColor = blue
            ),
            singleLine = true
        )
    }
}