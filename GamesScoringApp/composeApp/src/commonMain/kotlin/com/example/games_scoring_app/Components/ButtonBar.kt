package com.example.games_scoring_app.Components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed

@Composable
fun ButtonBar(
    onClick: () -> Unit,
    text: String,
    bgcolor: Color,
    textcolor: Color,
    width: Dp = 0.dp,
    height: Dp,
) {
    var buttonmodifier = Modifier
        .size(width = width, height = height)
    if(width == 0.dp){
        buttonmodifier = Modifier
            .fillMaxWidth()
    }
    Button(
        onClick = onClick,
        modifier = buttonmodifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgcolor,
            contentColor = textcolor
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = LeagueGothic,
                color = textcolor,
                fontSize = 40.sp,
            ),
            textAlign = TextAlign.Start
        )
    }
}