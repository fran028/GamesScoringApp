package com.example.games_scoring_app.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.white

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlayerAmountGrid(
    maxPlayers: Int,
    minPlayers: Int,
    selectedAmount: Int,
    onPlayerAmountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    bgcolor: Color = black,
    textcolor: Color = white,
    selectedbgcolor: Color = blue,
) {
    // We use BoxWithConstraints to get the width available for this component
    // instead of LocalConfiguration (which is Android-only).
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(darkgray, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ) {
        val screenWidth = maxWidth // maxWidth comes from BoxWithConstraints

        val buttonWidth = remember(maxPlayers, screenWidth) {
            // Subtracting padding (16dp total) and spacing between buttons
            (screenWidth - 16.dp - (maxPlayers - 1) * 4.dp) / maxPlayers
        }.coerceAtLeast(40.dp)

        Box(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            FlowRow(
                maxItemsInEachRow = maxPlayers,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                for (i in 1..maxPlayers) {
                    Button(
                        onClick = {
                            if (i in minPlayers..maxPlayers) {
                                onPlayerAmountSelected(i)
                            }
                        },
                        modifier = Modifier
                            .width(buttonWidth)
                            .height(64.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (i <= selectedAmount) selectedbgcolor else bgcolor,
                            contentColor = textcolor
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = i.toString(),
                                style = TextStyle(
                                    fontFamily = LeagueGothic,
                                    color = textcolor,
                                    fontSize = 48.sp,
                                    textAlign = TextAlign.Center,
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}