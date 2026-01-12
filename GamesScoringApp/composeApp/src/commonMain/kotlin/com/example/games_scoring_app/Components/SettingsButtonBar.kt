package com.example.games_scoring_app.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic

@Composable
fun SettingsButtonBar(
    onClick: () -> Unit,
    text: String,
    bgcolor: Color,
    textcolor: Color,
    width: Dp = 0.dp,
    height: Dp,
    icon: Int,
    iconSize: Dp = 24.dp,
    doubleIcon: Boolean = false
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(doubleIcon){
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Button Icon",
                    modifier = Modifier.size(iconSize)
                )
            }
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = LeagueGothic,
                    color = textcolor,
                    fontSize = 40.sp,
                ),
                textAlign = TextAlign.Start
            )
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Button Icon",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}