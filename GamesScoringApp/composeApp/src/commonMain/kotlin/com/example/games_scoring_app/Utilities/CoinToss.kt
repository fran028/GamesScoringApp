package com.example.games_scoring_app.Utilities

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CoinTosser() {
    var result by remember { mutableStateOf("Flip the coin!") }
    // This state now represents the final resting face of the coin
    var finalFaceIsHeads by remember { mutableStateOf(true) }

    // Animation state for the flip
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize() ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- START: Box container for the coin and result ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(darkgray, shape = RoundedCornerShape(10.dp))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // --- Coin Image with Flip Animation ---
                Image(
                    // --- FIX: Logic to determine which face to show during animation ---
                    painter = painterResource(
                        id =
                            // Check the rotation angle to decide which side is visible
                            if ((rotation.value % 360) in 90f..270f) {
                                // If the coin is 'flipped over' (between 90 and 270 degrees),
                                // show the opposite of its final resting face.
                                if (finalFaceIsHeads) R.drawable.coin_tails else R.drawable.coin_head
                            } else {
                                // Otherwise, show the final resting face.
                                if (finalFaceIsHeads) R.drawable.coin_head else R.drawable.coin_tails
                            }
                    ),
                    contentDescription = "Coin",
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer {
                            // Apply rotation on the Y-axis for a flipping effect
                            rotationY = rotation.value
                            // Optional: Make the camera seem further away to prevent clipping
                            cameraDistance = 12f * density
                        }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Result Text ---
                Text(
                    text = result,
                    fontFamily = LeagueGothic,
                    fontSize = 48.sp,
                    color = white
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Pushes the button to the bottom
        Spacer(modifier = Modifier.size(16.dp))
        // --- Flip Button ---
        Button(
            onClick = {
                // Prevent clicking while animation is running
                if (rotation.isRunning) return@Button

                coroutineScope.launch {
                    // First, decide the outcome
                    val flipToHeads = Random.nextBoolean()
                    finalFaceIsHeads = flipToHeads

                    // Start the flip animation (e.g., 3 full spins)
                    rotation.animateTo(
                        targetValue = rotation.value + 1080f, // 3 spins (3 * 360)
                        animationSpec = tween(durationMillis = 1000)
                    )
                    // Reset rotation to 0 without animation for the next flip
                    rotation.snapTo(0f)

                    // Set the result text *after* the animation is complete
                    result = if (flipToHeads) "HEADS" else "TAILS"
                }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = green,
                contentColor = white
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = "FLIP COIN",
                fontFamily = RobotoCondensed,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
