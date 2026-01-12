package com.example.games_scoring_app.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.white

@Composable
fun LoadingMessage(text: String = "LOADING", themeMode: Int, wheelColor: Color = blue) {

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white
    val image = if (themeMode == 0) R.drawable.logobig else R.drawable.logobig_negro

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp).fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Image(
            painter = painterResource(id = image),
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