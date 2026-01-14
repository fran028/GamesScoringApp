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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.darkgray

// KMP Resource Imports
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import gamesscoringapp.composeapp.generated.resources.*

@Composable
fun LastGameBox(
    onClick: () -> Unit,
    title: String,
    bgcolor: Color,
    textcolor: Color,
    accentColor: Color,
    width: Dp = 0.dp,
    icon: DrawableResource, // CHANGED: From Int to DrawableResource
    gameType: String = "",
    daysSinceLastPlayed: String
) {
    val iconSize = 32

    // --- ROW 1: Title and Play Button ---
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable { onClick() }, // Make the whole row clickable
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Box for Title (takes up available space)
        Box(
            modifier = Modifier
                .weight(1f) // Expands to fill available space
                .fillMaxHeight() // Make the title box fill the height of the Row
                .background(bgcolor, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // This Row contains the icon, title, and date
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(iconSize.dp)
                        .background(accentColor, shape = RoundedCornerShape(4.dp))
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        // KMP Syntax: Removed 'id ='
                        painter = painterResource(icon),
                        contentDescription = "Title Icon",
                        modifier = Modifier.size(if (gameType == "Generico") (iconSize * 0.25f).dp else (iconSize * 0.75f).dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Title Text
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = LeagueGothic,
                        color = textcolor,
                        fontSize = 40.sp,
                    ),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Date Text
                if(daysSinceLastPlayed != "") {
                    Text(
                        text = "($daysSinceLastPlayed)",
                        style = TextStyle(
                            fontFamily = RobotoCondensed,
                            color = textcolor.copy(alpha = 0.7f), // Make it slightly less prominent
                            fontSize = 18.sp,
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Box for Play Button
        Box(
            modifier = Modifier
                .fillMaxHeight() // Fill the height of the Row
                .aspectRatio(1f) // Make the width equal to the height, creating a square
                .background(accentColor, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .clickable { onClick() }, // The play button is also clickable
            contentAlignment = Alignment.Center
        ) {
            Image(
                // KMP Syntax: Replaced R.drawable.play with Res.drawable.play
                painter = painterResource(Res.drawable.play),
                contentDescription = "Play Game",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}