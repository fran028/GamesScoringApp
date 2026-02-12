package com.example.games_scoring_app.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.LoadingMessage
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.*
import com.example.games_scoring_app.Games.*
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.*
import com.example.games_scoring_app.Viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.time.Clock
import org.jetbrains.compose.resources.painterResource
import gamesscoringapp.composeapp.generated.resources.*
import kotlin.time.ExperimentalTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.games_scoring_app.Components.BindBackHandler


@OptIn(ExperimentalTime::class, ExperimentalComposeUiApi::class)
@Composable
fun GamePage(navController: NavController, gameId: Int, gameTypeId: Int) {
    if (gameTypeId == 0) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Home.route)
        }
        return
    }


    val haptic = LocalHapticFeedback.current

    val scrollState = rememberScrollState()
    val applicationScope = remember { CoroutineScope(SupervisorJob()) }
    // Removed LocalContext (Android only)
    val database = remember { AppDatabase.getDatabase(applicationScope) }
    val coroutineScope = rememberCoroutineScope()

    // --- ViewModel Setups ---
    val gamesRepository = remember { GamesRepository(database.gamesDao()) }
    val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModelFactory(gamesRepository))

    val gameTypesRepository = remember { GameTypesRepository(database.gameTypesDao()) }
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = GameTypesViewModelFactory(gameTypesRepository))

    val playersRepository = remember { PlayersRepository(database.playersDao()) }
    val playersViewModel: PlayersViewModel = viewModel(factory = PlayersViewModelFactory(playersRepository))

    val scoresRepository = remember { ScoresRepository(database.scoresDao()) }
    val scoresViewModel: ScoresViewModel = viewModel(factory = ScoresViewModelFactory(scoresRepository))

    val scoreTypesRepository = remember { ScoreTypesRepository(database.scoreTypesDao()) }
    val scoreTypesViewModel: ScoreTypesViewModel = viewModel(factory = ScoreTypesViewModelFactory(scoreTypesRepository))

    val settingsRepository = remember { SettingsRepository(database.settingsDao()) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsRepository))

    // --- State Variables ---
    val gameType by gameTypesViewModel.gameType.collectAsState()
    val playersWithScores by playersViewModel.getPlayersWithScores(gameId).collectAsState(initial = emptyList())
    val scoreTypes by scoreTypesViewModel.scoreTypesForGame.collectAsState()
    val themeMode by settingsViewModel.themeMode.collectAsState()

    // --- KMP Logic for "Press back again to exit" ---
    var backPressTime by remember { mutableStateOf(0L) }

    // Note: BackHandler in KMP requires the 'androidx.compose.runtime:runtime' or
    // 'org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose' which you have.
    // However, Toast is Android-only. For now, we print to console or use a Snackbar.
    BindBackHandler(enabled = true) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressTime < 2000) {
            navController.navigate(Screen.Home.route) {
                popUpTo(0)
            }
        } else {
            backPressTime = currentTime
        }
    }

    LaunchedEffect(key1 = gameId) {
        gameTypesViewModel.getGameTypeById(gameTypeId)
        scoreTypesViewModel.getScoreTypesByGameTypeId(gameTypeId)
        settingsViewModel.getThemeMode()
    }

    val onAddScore: (Scores) -> Unit = { newScore ->
        coroutineScope.launch { scoresViewModel.addNewScore(newScore) }
    }
    val onUpdateScore: (Scores) -> Unit = { scoreToUpdate ->
        coroutineScope.launch { scoresViewModel.updateScore(scoreToUpdate) }
    }
    val onDeleteScore: (Scores) -> Unit = { scoreToDelete ->
        coroutineScope.launch { scoresViewModel.deleteScore(scoreToDelete) }
    }

    // Replace R.drawable with Res.drawable (Compose Multiplatform Resources)
    val titelImage = when (gameType?.name) {
        "Generala" -> Res.drawable.dice_far
        "Truco" -> Res.drawable.fondo_cartas_truco
        "Points", "Ranking", "Levels" -> Res.drawable.papers
        else -> Res.drawable.fondo_cartas_truco
    }

    val accentColor = when (gameType?.name) {
        "Generala" -> blue
        "Truco" -> yellow
        "Points", "Ranking", "Levels" -> green
        else -> blue
    }

    val backgroundColor = if (themeMode == 0) black else cream

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if (gameType != null && playersWithScores.isNotEmpty()) {
            Spacer(modifier = Modifier.height(64.dp))
            // Use painterResource(titelImage)
            WidgetTitle(gameType!!.name.uppercase(), titelImage, navController)
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
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            coroutineScope.launch {
                                val newGameId = gamesViewModel.addNewGame(Games(id_GameType = gameTypeId))
                                playersWithScores.forEach {
                                    playersViewModel.addNewPlayer(Players(id_game = newGameId.toInt(), name = it.player.name))
                                }
                                navController.navigate(Screen.Game.createRoute(newGameId.toInt(), gameTypeId)) {
                                    popUpTo(Screen.Game.route) { inclusive = true }
                                }
                            }
                        }
                    )

                    ButtonBar(
                        text = "NEW GAME",
                        bgcolor = blue,
                        height = 64.dp,
                        textcolor = white,
                        width = 175.dp,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                            val currentPlayerNames = playersWithScores.map { it.player.name }
                            navController.navigate(Screen.SetUp.createRouteWithPlayers(gameTypeId, accentColor, currentPlayerNames))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                when (gameType!!.name) {
                    "Generala" -> GeneralaScoreboard(playersWithScores, scoreTypes, themeMode, onAddScore, onUpdateScore)
                    "Truco" -> TrucoScoreboard(playersWithScores, scoreTypes, gameType!!.maxScore, themeMode, onAddScore, onUpdateScore)
                    "Points" -> PuntosScoreboard(playersWithScores, scoreTypes, gameType!!.maxScore, themeMode, onAddScore, onUpdateScore, onDeleteScore)
                    "Ranking" -> RankingScoreboard(playersWithScores, scoreTypes, themeMode, onAddScore, onUpdateScore)
                    "Levels" -> LevelsScoreboard(playersWithScores, scoreTypes, themeMode, onAddScore, onUpdateScore)
                }
            }
        } else {
            LoadingMessage("LOADING", themeMode)
        }
    }
}
