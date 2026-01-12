package com.example.games_scoring_app.Components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalConfiguration
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
    val configuration = LocalConfiguration.current // Get screen configuration
    val screenWidth = configuration.screenWidthDp.dp // Get screen width in dp
    Log.d("PlayerAmountGrid", "screenWidth: $screenWidth")
    Log.d("PlayerAmountGrid", "maxPlayers: $maxPlayers")
    val buttonWidth = remember(maxPlayers) {
        (screenWidth - 32.dp - (maxPlayers) * 8.dp) / maxPlayers
    } .coerceAtLeast(40.dp)
    Log.d("PlayerAmountGrid", "buttonWidth: $buttonWidth")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(darkgray, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp) // Apply horizontal padding here
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        FlowRow(
            maxItemsInEachRow = maxPlayers,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (i in 1..maxPlayers) {
                Button(
                    onClick = { if (i in minPlayers..maxPlayers) {
                        onPlayerAmountSelected(i)
                    }},
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
                        modifier = Modifier.fillMaxSize(), // Fill the button's area
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = i.toString(),
                            style = TextStyle(
                                fontFamily = LeagueGothic,
                                color = textcolor,
                                fontSize = 48.sp, // Adjust as needed
                                textAlign = TextAlign.Center, // Ensure text is centered horizontally
                            ),
                            maxLines = 1,          // Limit to a single line
                            overflow = TextOverflow.Ellipsis, // Truncate with "..." if too long
                        )
                    }
                }
            }
        }
    }
}
