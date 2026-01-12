package com.example.games_scoring_app.Components

import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Theme.gold
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.FlowRow // No longer needed
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.red

@Composable
fun GameBox(
    onClick: () -> Unit,
    onDelete: () -> Unit,
    title: String,
    bgcolor: Color,
    textcolor: Color,
    accentColor: Color,
    width: Dp = 0.dp,
    icon: Int,
    gameType: String = "",
    daysSinceLastPlayed: String,
    players: List<Players>
) {
    val iconSize = 32

    // Main container for the entire component
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between the box and the player list
    ) {
        // --- Top Row: Delete Button, Content Box, Play Button ---
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
                    .width(48.dp)
                    .background(red, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.trash),
                    contentDescription = "Delete Game",
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
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // --- MODIFICATION: Use a Column to stack Title and Date ---
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Top part: Icon and Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(iconSize.dp)
                                .background(accentColor, shape = RoundedCornerShape(4.dp))
                                .clip(RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = "Title Icon",
                                modifier = Modifier.size(if (gameType == "Generico") (iconSize * 0.25f).dp else (iconSize * 0.75f).dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            style = TextStyle(
                                fontFamily = LeagueGothic,
                                color = textcolor,
                                fontSize = 40.sp,
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "($daysSinceLastPlayed)",
                            style = TextStyle(
                                fontFamily = RobotoCondensed,
                                color = textcolor.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                            ),
                            modifier = Modifier.padding(start = 4.dp) // Indent slightly
                        )
                    }
                    // Bottom part: Date, now correctly under the title

                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // --- 3. Play Button (Right Square) ---
            Box(
                // --- MODIFICATION: Use fillMaxHeight and aspectRatio to make it a square matching the row height ---
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(accentColor, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play Game",
                    modifier = Modifier.size(26.dp) // Icon size can be larger now
                )
            }
        }

        // --- Players List (Below the main row) ---
        if (players.isNotEmpty()) {
            val annotatedPlayerString = buildAnnotatedString {
                val winner = players.find { it.won }
                val otherPlayers = players.filter { !it.won }

                if (winner != null) {
                    withStyle(style = SpanStyle(color = gold, fontWeight = FontWeight.Bold)) {
                        append(winner.name)
                    }
                    if (otherPlayers.isNotEmpty()) {
                        append(" / ")
                    }
                }
                append(otherPlayers.joinToString(" / ") { it.name })
            }

            // --- MODIFICATION START ---
            // Wrap the Text in a Box with a background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkgray, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp), // Add padding inside the box
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = annotatedPlayerString,
                    style = TextStyle(
                        fontFamily = RobotoCondensed,
                        color = textcolor.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                    ),
                    textAlign = TextAlign.Center
                )
            }
            // --- MODIFICATION END ---
        }
    }
}
