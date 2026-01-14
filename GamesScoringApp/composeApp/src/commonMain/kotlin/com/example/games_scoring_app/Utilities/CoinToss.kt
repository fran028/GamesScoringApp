package com.example.games_scoring_app.Utilities

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// KMP Resource Imports
import org.jetbrains.compose.resources.painterResource
import gamesscoringapp.composeapp.generated.resources.*

import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CoinTosser() {
    var result by remember { mutableStateOf("Flip the coin!") }
    var finalFaceIsHeads by remember { mutableStateOf(true) }

    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    // KMP: painterResource used without 'id =' and using Res.drawable
                    painter = painterResource(
                        if ((rotation.value % 360) in 90f..270f) {
                            if (finalFaceIsHeads) Res.drawable.coin_tails else Res.drawable.coin_head
                        } else {
                            if (finalFaceIsHeads) Res.drawable.coin_head else Res.drawable.coin_tails
                        }
                    ),
                    contentDescription = "Coin",
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer {
                            rotationY = rotation.value
                            cameraDistance = 12f * density
                        }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = result,
                    fontFamily = LeagueGothic,
                    fontSize = 48.sp,
                    color = white
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = {
                if (rotation.isRunning) return@Button

                coroutineScope.launch {
                    val flipToHeads = Random.nextBoolean()
                    finalFaceIsHeads = flipToHeads

                    rotation.animateTo(
                        targetValue = rotation.value + 1080f,
                        animationSpec = tween(durationMillis = 1000)
                    )
                    rotation.snapTo(0f)
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