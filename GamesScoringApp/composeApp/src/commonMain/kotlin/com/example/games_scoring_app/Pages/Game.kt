package com.example.games_scoring_app.Pages

import android.os.Build
import android.util.Log
import android.widget.Toast // <-- ADD THIS IMPORT
import androidx.activity.compose.BackHandler // <-- ADD THIS IMPORT
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.LoadingMessage
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.*
import com.example.games_scoring_app.Games.GeneralaScoreboard
import com.example.games_scoring_app.Games.LevelsScoreboard
import com.example.games_scoring_app.Games.PuntosScoreboard
import com.example.games_scoring_app.Games.RankingScoreboard
import com.example.games_scoring_app.Games.TrucoScoreboard
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.cream
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.toTypedArray

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamePage(navController: NavController, gameId: Int, gameTypeId: Int ) {
    if(gameTypeId == 0) {
        Log.d("GamePage", "Navigating to Home screen")
        navController.navigate(Screen.Home.route)
        return // Add return to stop execution after navigation
    }
    val TAG = "GamePage"
    Log.d(TAG, "--- GAME STARTED (Game ID: $gameId) ---")
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)
    val coroutineScope = rememberCoroutineScope()

    // --- ViewModel Setups ---
    val gamesRepository = GamesRepository(database.gamesDao())
    val gamesViewModelFactory = GamesViewModelFactory(gamesRepository)
    val gamesViewModel: GamesViewModel = viewModel(factory = gamesViewModelFactory)

    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModelFactory = GameTypesViewModelFactory(gameTypesRepository)
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = gameTypesViewModelFactory)

    val playersRepository = PlayersRepository(database.playersDao())
    val playersViewModelFactory = PlayersViewModelFactory(playersRepository)
    val playersViewModel: PlayersViewModel = viewModel(factory = playersViewModelFactory)

    val scoresRepository = ScoresRepository(database.scoresDao())
    val scoresViewModelFactory = ScoresViewModelFactory(scoresRepository)
    val scoresViewModel: ScoresViewModel = viewModel(factory = scoresViewModelFactory)

    // NEW: ViewModel for fetching the categories of scores (e.g., "Aces", "Twos").
    val scoreTypesRepository = ScoreTypesRepository(database.scoreTypesDao())
    val scoreTypesViewModelFactory = ScoreTypesViewModelFactory(scoreTypesRepository)
    val scoreTypesViewModel: ScoreTypesViewModel = viewModel(factory = scoreTypesViewModelFactory)

    val settingsRepository = SettingsRepository(database.settingsDao())
    val settingsViewModelFactory = SettingsViewModelFactory(settingsRepository)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    Log.d(TAG, "ViewModels setup finish")

    // --- State Variables ---
    val gameType by gameTypesViewModel.gameType.collectAsState()
    val playersWithScores by playersViewModel.getPlayersWithScores(gameId).collectAsState(initial = emptyList())
    // NEW: Fetch all possible score types for this game (e.g., a list of "Aces", "Full House", etc.).
    val scoreTypes by scoreTypesViewModel.scoreTypesForGame.collectAsState()
    val themeMode by settingsViewModel.themeMode.collectAsState()

    // --- ADDED: Logic for "Press back again to exit" ---
    var backPressTime by remember { mutableStateOf(0L) }
    val toastMessage = "Press back again to exit"

    BackHandler(enabled = true) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressTime < 2000) { // If pressed within 2 seconds
            // Navigate to Home and clear the back stack
            navController.navigate(Screen.Home.route) {
                popUpTo(0) // Clears the entire back stack
            }
        } else {
            backPressTime = currentTime
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }
    // --- End of added logic ---

    Log.d(TAG, "Variables initialized")

    // --- Effects ---
    LaunchedEffect(key1 = gameId) {
        Log.d(TAG, "LaunchedEffect called for gameId: $gameId")
        gameTypesViewModel.getGameTypeById(gameTypeId)
        // NEW: Fetch the relevant score types when the game starts.
        scoreTypesViewModel.getScoreTypesByGameTypeId(gameTypeId)
        settingsViewModel.getThemeMode()
        Log.d(TAG, "LaunchedEffect finished")
    }

    val onAddScore: (Scores) -> Unit = { newScore ->
        coroutineScope.launch {
            scoresViewModel.addNewScore(newScore)
            Log.d(TAG, "Adding new score for Player ID: ${newScore.id_player}, Score: ${newScore.score}")
        }
    }
    val onUpdateScore: (Scores) -> Unit = { scoreToUpdate ->
        coroutineScope.launch {
            scoresViewModel.updateScore(scoreToUpdate)
            Log.d(TAG, "Updating score for Player ID: ${scoreToUpdate.id_player}, New Score: ${scoreToUpdate.score}")
        }
    }

    val onDeleteScore: (Scores) -> Unit = { scoreToDelete ->
        coroutineScope.launch { // This moves the operation to a background thread
            scoresViewModel.deleteScore(scoreToDelete)
        }
    }



    val titelImage = remember(gameType?.name) {
        when (gameType?.name) {
            "Generala" -> mutableStateOf(R.drawable.dice_far)
            "Truco" -> mutableStateOf(R.drawable.fondo_cartas_truco)
            "Points", "Ranking", "Levels" -> mutableStateOf(R.drawable.papers)
            else -> mutableStateOf(R.drawable.fondo_cartas_truco) // Default image
        }
    }
    val accentColor = when (gameType?.name) {
        "Generala" -> blue
        "Truco" -> yellow
        "Points", "Ranking", "Levels" -> green
        else -> blue
    }

    val backgroundColor = if (themeMode == 0) black else cream
    val fontColor = if (themeMode == 0) white else black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // Use playersWithScores to check if data is loaded
        if (gameType != null && playersWithScores.isNotEmpty()) {
            Spacer(modifier = Modifier.height(64.dp))
            WidgetTitle(gameType!!.name.uppercase(), titelImage.value, navController)
            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonBar(
                        text = "RESTART",
                        bgcolor = green,
                        height = 64.dp,
                        textcolor = white,
                        width = 175.dp,
                        onClick = {
                            Log.d(TAG, "NEW GAME button clicked")
                            coroutineScope.launch {
                                // 1. Create a new game with the same type
                                val newGame = Games(id_GameType = gameTypeId)
                                val newGameId = gamesViewModel.addNewGame(newGame)

                                // 2. Create players for the new game. No initial scores are created here,
                                // the scoreboard will handle creating them as needed.
                                val newPlayerNames =
                                    playersWithScores.map { it.player.name }.toTypedArray()
                                newPlayerNames.forEach { name ->
                                    val newPlayer =
                                        Players(id_game = newGameId.toInt(), name = name)
                                    playersViewModel.addNewPlayer(newPlayer)
                                }
                                Log.d(TAG, "Created new game with ID: $newGameId")

                                // 3. Navigate to the new game screen and remove the old one from the back stack
                                navController.navigate(
                                    Screen.Game.createRoute(
                                        newGameId.toInt(),
                                        gameTypeId
                                    )
                                ) {
                                    popUpTo(Screen.Game.route) { inclusive = true }
                                }
                            }
                        }
                    )

                    ButtonBar(
                        text = "EDIT",
                        bgcolor = blue,
                        height = 64.dp,
                        textcolor = white,
                        width = 175.dp,
                        onClick = {
                            Log.d(TAG, "Edit button clicked")
                            val currentPlayerNames = playersWithScores.map{it.player.name}
                            navController.navigate(
                                Screen.SetUp.createRouteWithPlayers(
                                    gameTypeId,
                                    accentColor,
                                    currentPlayerNames
                                )
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Pass all relevant data to the scoreboards
                when (gameType!!.name) {
                    "Generala" -> {
                        // Generala needs the list of score types to build its rows.
                        GeneralaScoreboard(playersWithScores, scoreTypes, themeMode, onAddScore, onUpdateScore)
                    }
                    "Truco" -> {
                        // Truco may only need to find the one "final" score type.
                        TrucoScoreboard(playersWithScores, scoreTypes, gameType!!.maxScore, themeMode, onAddScore, onUpdateScore)
                    }
                    "Points" -> {
                        // --- MODIFIED: Pass the onDeleteScore lambda ---
                        PuntosScoreboard(
                            playersWithScores = playersWithScores,
                            scoreTypes = scoreTypes,
                            maxScore = gameType!!.maxScore,
                            themeMode = themeMode,
                            onAddScore = onAddScore,
                            onUpdateScore = onUpdateScore,
                            onDeleteScore = onDeleteScore // <-- Pass the new function here
                        )
                    }
                    "Ranking" -> {
                        RankingScoreboard(playersWithScores, scoreTypes, themeMode, onAddScore, onUpdateScore)
                    }
                    "Levels" -> {
                        LevelsScoreboard(playersWithScores, scoreTypes, themeMode, onAddScore, onUpdateScore)
                    }
                }
            }
        }  else {
            LoadingMessage("LOADING", themeMode)
        }
    }
}
