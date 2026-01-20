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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(darkgray, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ) {
        val haptic = LocalHapticFeedback.current
        val screenWidth = maxWidth
        val spacing = 4.dp
        val horizontalPadding = 32.dp // Total padding (8.dp left + 8.dp right)

        // Calculate width based on 8 items to keep button size consistent across rows
        val buttonWidth = remember(screenWidth) {
            (screenWidth - horizontalPadding - (7 * spacing)) / 8
        }.coerceAtLeast(40.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 8,
                // Arrangement.Center centers the items in the row if they don't fill the max width
                horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) {
                for (i in 1..maxPlayers) {
                    val isEnabled = i in minPlayers..maxPlayers
                    Button(
                        onClick = {
                            if (isEnabled) {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                onPlayerAmountSelected(i)
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                            }
                        },
                        modifier = Modifier
                            .width(buttonWidth)
                            .height(64.dp),
                        shape = RoundedCornerShape(5.dp),
                        // Dim the color if the player amount is below the minimum required
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (i <= selectedAmount) selectedbgcolor else bgcolor,
                            contentColor = if (isEnabled) textcolor else textcolor.copy(alpha = 0.3f)
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