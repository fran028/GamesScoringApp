package com.example.games_scoring_app.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // In KMP, we use the generated Res object instead of R.drawable
    val image = if (themeMode == 0) Res.drawable.logobig else Res.drawable.logobig_negro

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp).fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Image(
            // KMP Syntax: removed 'id =' parameter
            painter = painterResource(image),
            contentDescription = "App Image",
            modifier = Modifier.size(100.dp)
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
        CircularProgressIndicator(color = wheelColor)
    }
}