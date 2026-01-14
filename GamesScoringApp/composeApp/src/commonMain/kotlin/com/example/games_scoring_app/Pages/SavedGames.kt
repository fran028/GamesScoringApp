package com.example.games_scoring_app.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// KMP Navigation and Resource Imports
import androidx.navigation.NavController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import gamesscoringapp.composeapp.generated.resources.*

import com.example.games_scoring_app.Components.GameBox
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.*
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.*
import com.example.games_scoring_app.Viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@Composable
fun SavedGamesPage(navController: NavController) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val applicationScope = remember { CoroutineScope(SupervisorJob()) }
    // KMP Database does not need Context
    val database = remember { AppDatabase.getDatabase(applicationScope) }

    val gamesRepository = remember { GamesRepository(database.gamesDao()) }
    val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModelFactory(gamesRepository))
    val gameTypesRepository = remember { GameTypesRepository(database.gameTypesDao()) }
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = GameTypesViewModelFactory(gameTypesRepository))
    val settingsRepository = remember { SettingsRepository(database.settingsDao()) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsRepository))

    val allGamesWithPlayers by gamesViewModel.allGamesWithPlayers.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()
    val themeMode by settingsViewModel.themeMode.collectAsState()

    var gamesLimit by remember { mutableStateOf(10) }
    val initialGamesLimit = 10

    var showDeleteDialog by remember { mutableStateOf(false) }
    var gameToDelete by remember { mutableStateOf<Games?>(null) }
    var gameTypeNameToDelete by remember { mutableStateOf("") }

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black

    LaunchedEffect(Unit) {
        gamesViewModel.getAllGamesWithPlayers()
        gameTypesViewModel.getAllGameTypes()
        settingsViewModel.getThemeMode()
    }

    if (showDeleteDialog && gameToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                gameToDelete = null
                gameTypeNameToDelete = ""
            },
            title = { Text("Confirm Deletion", fontFamily = LeagueGothic, fontSize = 32.sp) },
            text = { Text("Are you sure you want to permanently delete this game? This action cannot be undone.", fontFamily = RobotoCondensed, fontSize = 16.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        gameToDelete?.let { game ->
                            gamesViewModel.deleteGame(game)
                            // Note: Toast is removed as it is Android-specific.
                            // You can implement a KMP Snackbar here instead.
                        }
                        showDeleteDialog = false
                        gameToDelete = null
                        gameTypeNameToDelete = ""
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = red, contentColor = darkgray)
                ) {
                    Text("DELETE", fontFamily = LeagueGothic, fontSize = 18.sp)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        gameToDelete = null
                        gameTypeNameToDelete = ""
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = yellow, contentColor = darkgray)
                ) {
                    Text("CANCEL", fontFamily = LeagueGothic, fontSize = 18.sp)
                }
            },
            containerColor = darkgray,
            titleContentColor = white,
            textContentColor = white.copy(alpha = 0.8f)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Changed R.drawable.papers to Res.drawable.papers
        WidgetTitle("SCORES", Res.drawable.papers, navController)

        Spacer(modifier = Modifier.height(20.dp))
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ){
            Text(text = "Games Played", fontFamily = LeagueGothic, fontSize = 48.sp, color = fontColor)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "(${allGamesWithPlayers.size})", fontFamily = LeagueGothic, fontSize = 24.sp, color = gray, modifier = Modifier.padding(bottom = 4.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            if (allGamesWithPlayers.isNotEmpty() && gameTypes.isNotEmpty()) {
                allGamesWithPlayers.take(gamesLimit).forEach { gameWithPlayers ->
                    val game = gameWithPlayers.game
                    val gameType = gameTypes.find { it?.id == game.id_GameType }

                    if (gameType != null) {
                        // Use DrawableResource instead of Int
                        val (buttonIcon, accentColor) = when (gameType.type) {
                            "Dados" -> Res.drawable.dices to blue
                            "Cartas" -> Res.drawable.card to yellow
                            "Generico" -> Res.drawable.paper to green
                            else -> Res.drawable.paper to white
                        }

                        GameBox(
                            title = gameType.name.uppercase(),
                            onClick = {
                                navController.navigate(Screen.Game.createRoute(game.id, game.id_GameType))
                            },
                            bgcolor = darkgray,
                            textcolor = white,
                            accentColor = accentColor,
                            icon = buttonIcon,
                            gameType = gameType.type,
                            daysSinceLastPlayed = game.date,
                            players = gameWithPlayers.players,
                            onDelete = {
                                gameToDelete = game
                                gameTypeNameToDelete = gameType.name
                                showDeleteDialog = true
                            },
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                if (allGamesWithPlayers.size > initialGamesLimit) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (gamesLimit > initialGamesLimit) {
                            Button(
                                onClick = { gamesLimit = (gamesLimit - 10).coerceAtLeast(initialGamesLimit) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = darkgray)
                            ) {
                                Text("SEE LESS", fontFamily = LeagueGothic, fontSize = 24.sp, color = white)
                            }
                        }
                        if (allGamesWithPlayers.size > gamesLimit) {
                            Button(
                                onClick = { gamesLimit += 10 },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = darkgray)
                            ) {
                                Text("SEE MORE", fontFamily = LeagueGothic, fontSize = 24.sp, color = white)
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "No games played yet",
                    fontFamily = LeagueGothic,
                    fontSize = 24.sp,
                    color = fontColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}