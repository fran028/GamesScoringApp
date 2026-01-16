package com.example.games_scoring_app.Utilities

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import gamesscoringapp.composeapp.generated.resources.Res
import gamesscoringapp.composeapp.generated.resources.*

@Composable
fun Timer() {
    var totalTime by remember { mutableStateOf(60L) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = timeLeft, key2 = isRunning) {
        if (isRunning && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft == 0L) {
            isRunning = false
        }
    }

    val timeOptions = listOf(10L, 30L, 60L, 120L)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. TIME SELECTION (TOP) ---
        Text(
            text = "SELECT TIME (SECONDS)",
            fontFamily = LeagueGothic,
            fontSize = 24.sp,
            color = white
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            timeOptions.forEach { time ->
                Button(
                    onClick = { if (!isRunning) { totalTime = time; timeLeft = time } },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (totalTime == time) blue else gray,
                        contentColor = if (totalTime == time) black else white
                    ),
                    enabled = !isRunning
                ) {
                    Text(text = time.toString(), fontFamily = LeagueGothic, fontSize = 24.sp)
                }
            }
        }

        // PUSHES CONTENT DOWN
        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. MIDDLE SECTION (HOURGLASS + BUTTONS) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // FIXED HEIGHT prevents overlapping title bar
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hourglass
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
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
                        val currentWidthHalf = androidx.compose.ui.util.lerp(w / 2, neckWidthHalf, fillPercentage)
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(centerX - currentWidthHalf, sandLevelY)
                            lineTo(centerX + currentWidthHalf, sandLevelY)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }
                        drawPath(path, color = yellow)
                    }

                    // Top Sand
                    if (progress > 0) {
                        val sandLevelY = (h / 2) * (1f - progress)
                        val currentWidthHalf = androidx.compose.ui.util.lerp(neckWidthHalf, w / 2, progress)
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(centerX - currentWidthHalf, sandLevelY)
                            lineTo(centerX + currentWidthHalf, sandLevelY)
                            lineTo(centerX + neckWidthHalf, centerY)
                            lineTo(centerX - neckWidthHalf, centerY)
                            close()
                        }
                        drawPath(path, color = yellow)
                    }

                    // Falling Stream (Dotted/Circles)
                    if (isRunning && timeLeft > 0) {
                        val bottomSandLevelY = h - ((h / 2) * (1f - progress))
                        drawLine(
                            color = yellow,
                            start = Offset(centerX - neckWidthHalf/2, centerY),
                            end = Offset(centerX - neckWidthHalf/2, h),
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
                            start = Offset(centerX + neckWidthHalf/2, centerY + neckWidthHalf),
                            end = Offset(centerX + neckWidthHalf/2, h - neckWidthHalf),
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
                            pathEffect = androidx.compose.ui.graphics.PathEffect.cornerPathEffect(20f) // Increased for more noticeable rounding
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Buttons
            Column(
                modifier = Modifier.width(80.dp), // Width instead of weight to prevent squishing
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier.size(60.dp).background(if (isRunning) yellow else green, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        painter = painterResource(if (isRunning) Res.drawable.pause else Res.drawable.play),
                        contentDescription = "Toggle",
                        tint = white,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(
                    onClick = { isRunning = false; timeLeft = totalTime },
                    modifier = Modifier.size(60.dp).background(red, RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.undo),
                        contentDescription = "Reset",
                        tint = white,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        // PUSHES CLOCK TO BOTTOM
        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. NUMERICAL CLOCK (BOTTOM) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkgray, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ){
            Text(
                text = "${(timeLeft / 60).toString().padStart(2, '0')}:${(timeLeft % 60).toString().padStart(2, '0')}",
                fontFamily = LeagueGothic,
                fontSize = 90.sp,
                color = white,
                modifier = Modifier.align(alignment = Alignment.Center).fillMaxWidth()
            )
        }
    }
}