package com.example.games_scoring_app.Components

import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Theme.gold
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.red

// KMP Resource Imports
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import gamesscoringapp.composeapp.generated.resources.*

@Composable
fun GameBox(
    onClick: () -> Unit,
    onDelete: () -> Unit,
    title: String,
    bgcolor: Color,
    textcolor: Color,
    accentColor: Color,
    width: Dp = 0.dp,
    icon: DrawableResource, // CHANGED: From Int to DrawableResource
    gameType: String = "",
    daysSinceLastPlayed: String,
    players: List<Players>
) {
    val iconSize = 32

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- 1. Delete Button ---
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(red, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    // KMP Syntax: Use Res.drawable instead of R.drawable
                    painter = painterResource(Res.drawable.trash),
                    contentDescription = "Delete Game",
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // --- 2. Main Content Box ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(bgcolor, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(iconSize.dp)
                                .background(accentColor, shape = RoundedCornerShape(4.dp))
                                .clip(RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                // KMP Syntax: Pass the DrawableResource directly
                                painter = painterResource(icon),
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
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // --- 3. Play Button ---
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .background(accentColor, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    // KMP Syntax: Use Res.drawable
                    painter = painterResource(Res.drawable.play),
                    contentDescription = "Play Game",
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // --- Players List ---
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkgray, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
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
        }
    }
}