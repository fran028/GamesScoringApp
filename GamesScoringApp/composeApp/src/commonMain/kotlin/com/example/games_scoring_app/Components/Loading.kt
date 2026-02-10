package com.example.games_scoring_app.Components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.white

// KMP Resource Imports
import org.jetbrains.compose.resources.painterResource
import gamesscoringapp.composeapp.generated.resources.*

@Composable
fun LoadingMessage(text: String = "LOADING", themeMode: Int, wheelColor: Color = blue) {

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black

    val image = Res.drawable.logo

    // Infinite Rotation Animation
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sandclockRotation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp).fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Image(
            painter = painterResource(image),
            contentDescription = "App Image",
            modifier = Modifier.size(125.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = text,
            style = TextStyle(
                fontFamily = LeagueGothic,
                fontSize = 60.sp,
                color = fontColor
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        Icon(
            painter = painterResource(Res.drawable.sand_clock), // Ensure this exists in your commonMain/composeResources/drawable
            contentDescription = "Loading",
            tint = wheelColor,
            modifier = Modifier
                .size(64.dp)
                .rotate(rotation)
        )
    }
}