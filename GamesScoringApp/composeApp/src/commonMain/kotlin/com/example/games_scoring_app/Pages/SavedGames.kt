package com.example.games_scoring_app.Pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.GameBox
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypes
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.Games
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.GamesViewModel
import com.example.games_scoring_app.Viewmodel.GamesViewModelFactory
import com.example.games_scoring_app.Viewmodel.SettingsViewModel
import com.example.games_scoring_app.Viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Composable
fun SavedGamesPage(navController: NavController) {
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)

    val gamesRepository = GamesRepository(database.gamesDao())
    val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModelFactory(gamesRepository))
    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = GameTypesViewModelFactory(gameTypesRepository))
    val settingsRepository = SettingsRepository(database.settingsDao())
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsRepository))

    val allGamesWithPlayers by gamesViewModel.allGamesWithPlayers.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()
    val themeMode by settingsViewModel.themeMode.collectAsState()

    var gamesLimit by remember { mutableStateOf(10) }
    val initialGamesLimit = 10

    // --- State for the confirmation dialog ---
    var showDeleteDialog by remember { mutableStateOf(false) }
    var gameToDelete by remember { mutableStateOf<Games?>(null) }
    // --- FIX: Add state to hold the name of the game type for the toast message ---
    var gameTypeNameToDelete by remember { mutableStateOf("") }


    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black

    LaunchedEffect(key1 = Unit) {
        gamesViewModel.getAllGamesWithPlayers()
        gameTypesViewModel.getAllGameTypes()
        settingsViewModel.getThemeMode()
    }

    // --- Composable for the confirmation dialog ---
    if (showDeleteDialog && gameToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                gameToDelete = null
                gameTypeNameToDelete = "" // Reset the name
            },
            title = { Text("Confirm Deletion", fontFamily = LeagueGothic, fontSize = 32.sp) },
            text = { Text("Are you sure you want to permanently delete this game? This action cannot be undone.", fontFamily = RobotoCondensed, fontSize = 16.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        gameToDelete?.let { game ->
                            // The ViewModel handles the background thread correctly
                            gamesViewModel.deleteGame(game)

                            // --- FIX: Use the stored game type name for the toast ---
                            Toast.makeText(context, "$gameTypeNameToDelete deleted", Toast.LENGTH_SHORT).show()
                        }
                        showDeleteDialog = false
                        gameToDelete = null
                        gameTypeNameToDelete = "" // Reset the name
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = red,
                        contentColor = darkgray
                    )
                ) {
                    Text("DELETE", fontFamily = LeagueGothic, fontSize = 18.sp)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        gameToDelete = null
                        gameTypeNameToDelete = "" // Reset the name
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = yellow, // Use gray for cancel button
                        contentColor = darkgray
                    )
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
        WidgetTitle("SCORES", R.drawable.papers, navController);
        Spacer(modifier = Modifier.height(20.dp))
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ){
            Text(
                text = "Games Played",
                fontFamily = LeagueGothic,
                fontSize = 48.sp,
                color = fontColor
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "(${allGamesWithPlayers.size})",
                fontFamily = LeagueGothic,
                fontSize = 24.sp,
                color = gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(Modifier.padding(horizontal = 16.dp)) {
            if (allGamesWithPlayers.isNotEmpty() && gameTypes.isNotEmpty()) {
                allGamesWithPlayers.take(gamesLimit).forEach { gameWithPlayers ->
                    val game = gameWithPlayers.game
                    val gameType = gameTypes.find { it?.id == game.id_GameType } // Corrected the foreign key name based on your likely schema

                    if (gameType != null) {
                        var buttonIconId = 0
                        var accentColor = yellow
                        when (gameType.type) {
                            "Dados" -> {
                                buttonIconId = R.drawable.dices
                                accentColor = blue
                            }
                            "Cartas" -> {
                                buttonIconId = R.drawable.card
                                accentColor = yellow
                            }
                            "Generico" -> {
                                buttonIconId = R.drawable.paper
                                accentColor = green
                            }
                            else -> {
                                buttonIconId = R.drawable.paper
                                accentColor = white
                            }
                        }
                        GameBox(
                            title = gameType.name.uppercase(),
                            onClick = {
                                navController.navigate(
                                    Screen.Game.createRoute( // Corrected from Screen.Game to Screen.Match based on previous interactions
                                        game.id,
                                        game.id_GameType
                                    )
                                )
                            },
                            bgcolor = darkgray,
                            textcolor = white,
                            accentColor = accentColor,
                            icon = buttonIconId,
                            gameType = gameType.type,
                            daysSinceLastPlayed = game.date,
                            players = gameWithPlayers.players,
                            onDelete = {
                                // --- FIX: Store both the game and its type name for the dialog ---
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
