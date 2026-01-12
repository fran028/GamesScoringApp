package com.example.games_scoring_app.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen

@Composable
fun PageTitle(title: String, image: Int, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(0.dp)
            .background(Color.Transparent, /*shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp)*/)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.25f))
                    )
                )
        ) {
            Column(verticalArrangement = Arrangement.Bottom) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.logobig),
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
                        painter = painterResource(id = R.drawable.setting_line),
                        contentDescription = "App Image",
                        modifier = Modifier.size(55.dp)
                            .clickable {
                                navController.navigate(Screen.Settings.route)
                            }
                    )
                }
            }
        }
    }
}