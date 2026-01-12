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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.gray


@Composable
fun UtilitiesBox(
    onClick: () -> Unit,
    title: String,
    description: String,
    bgcolor: Color,
    textcolor: Color,
    accentColor: Color,
    width: Dp = 0.dp,
    icon: Int,
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(bgcolor, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() } // The main body is clickable
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
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
                            painter = painterResource(id = icon),
                            contentDescription = "Title Icon",
                            modifier = Modifier.size((iconSize * 0.75f).dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = TextStyle(
                            fontFamily = LeagueGothic,
                            color = textcolor,
                            fontSize = 40.sp,
                        ),
                        textAlign = TextAlign.Start,
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f) // Make it a square
                    .background(accentColor, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play Game",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (description.isNotBlank()) {
                Text(
                    text = description,
                    style = TextStyle(
                        fontFamily = RobotoCondensed,
                        color = gray,
                        fontSize = 16.sp,
                    ),
                    textAlign = TextAlign.Start
                )
            }
            // }
        }
    }
}
