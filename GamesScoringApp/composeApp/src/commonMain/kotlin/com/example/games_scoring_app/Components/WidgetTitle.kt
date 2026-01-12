package com.example.games_scoring_app.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.white

@Composable
fun WidgetTitle(title: String, image: Int, navController: NavController) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 8.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(4.dp, black, RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.25f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App Image",
                        modifier = Modifier.size(50.dp)
                            .clickable {
                                navController.navigate(Screen.Home.route)
                            }
                    )
                    Text(
                        text = title,
                        style = TextStyle(
                            fontFamily = LeagueGothic,
                            fontSize = 48.sp,
                            color = white,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 3f
                            )
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    /*Image(
                        painter = painterResource(id = R.drawable.scoreboard),
                        contentDescription = "App Image",
                        modifier = Modifier.size(60.dp)
                            .clickable {
                                navController.navigate(Screen.SavedGames.route)
                            },

                    )*/
                    Image(
                        painter = painterResource(id = R.drawable.scoreboard_white),
                        contentDescription = "Games List Icon",
                        modifier = Modifier.size(55.dp)
                            .clickable {
                                navController.navigate(Screen.SavedGames.route)
                            }
                    )
                }
            }
        }
    }
}
