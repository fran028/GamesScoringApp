package com.example.games_scoring_app.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.red

@Composable
fun GameTypeTitle(
    title: String,
    icon: Int,
    accentColor: Color,
    textcolor: Color,
    bgcolor: Color,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- 1. Delete Button (Left Square) ---
        Box(
            // --- MODIFICATION: Use fillMaxHeight and aspectRatio to make it a square matching the row height ---
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(accentColor, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "GameType Icon Left",
                modifier = Modifier.size(32.dp) // Icon size can be larger now
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // --- 2. Main Content Box (Center) ---
        Box(
            modifier = Modifier
                .weight(1f) // Expands to fill available space
                .fillMaxHeight()
                .background(bgcolor, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = LeagueGothic,
                    color = textcolor,
                    fontSize = 40.sp,
                )
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Box(
             modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .background(accentColor, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "GameType Icon Right",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}