package com.example.games_scoring_app.Pages

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.games_scoring_app.Components.ButtonDateBar
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.IconButtonBar
import com.example.games_scoring_app.Components.LastGameBox
import com.example.games_scoring_app.R
import com.example.games_scoring_app.Components.PageTitle
import com.example.games_scoring_app.Components.ScoreBoardBox
import com.example.games_scoring_app.Components.UtilitiesBox
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameStats
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.black
import com.example.games_scoring_app.Theme.white
import com.example.games_scoring_app.Theme.LeagueGothic
import com.example.games_scoring_app.Theme.RobotoCondensed
import com.example.games_scoring_app.Theme.blue
import com.example.games_scoring_app.Theme.cream
import com.example.games_scoring_app.Theme.darkgray
import com.example.games_scoring_app.Theme.gray
import com.example.games_scoring_app.Theme.green
import com.example.games_scoring_app.Theme.red
import com.example.games_scoring_app.Theme.yellow
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.GamesViewModel
import com.example.games_scoring_app.Viewmodel.GamesViewModelFactory
import com.example.games_scoring_app.Viewmodel.SettingsViewModel
import com.example.games_scoring_app.Viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


@Composable
fun HomePage(navController: NavController) {
    val appName = stringResource(id = R.string.app_name)
    var noGames = true
    val scrollState = rememberScrollState()

    val applicationScope = CoroutineScope(SupervisorJob())
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context, applicationScope)

    val gamesRepository = GamesRepository(database.gamesDao())
    val gamesViewModelFactory = GamesViewModelFactory(gamesRepository)
    val gamesViewModel: GamesViewModel = viewModel(factory = gamesViewModelFactory)

    val gameTypesRepository = GameTypesRepository(database.gameTypesDao())
    val gameTypesViewModelFactory = GameTypesViewModelFactory(gameTypesRepository)
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = gameTypesViewModelFactory)

    val settingsRepository = SettingsRepository(database.settingsDao())
    val settingsViewModelFactory = SettingsViewModelFactory(settingsRepository)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    val themeMode by settingsViewModel.themeMode.collectAsState()
    val lastGame by gamesViewModel.lastGame.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()
    val gameStats by gamesViewModel.gameStats.collectAsState()
    var lastGameTypeName by remember { mutableStateOf("") }
    var lastGameType by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit) {
        gamesViewModel.getLastGame()
        gameTypesViewModel.getAllGameTypes()
        settingsViewModel.getThemeMode()
    }

    LaunchedEffect(key1 = lastGame, key2 = gameTypes) {
        lastGame?.let { game ->
            // Find the game type name from the list
            lastGameTypeName = gameTypes.find { it?.id == game.id_GameType }?.name ?: ""
            lastGameType = gameTypes.find { it?.id == game.id_GameType }?.type ?: ""
        }
    }


    LaunchedEffect(key1 = gameTypes) {
        gameTypes.forEach { gameType ->
            gamesViewModel.getStatsForGameType(gameType!!.id)
        }
    }

    Log.d("HomePage", "themeMode: $themeMode")
    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState)
            .padding(0.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        WidgetTitle(appName.uppercase(), R.drawable.game_topview, navController);
        if(lastGame != null) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Last Game",
                fontFamily = RobotoCondensed,
                fontSize = 36.sp,
                color = fontColor,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column (Modifier.padding(horizontal = 16.dp )) {
                lastGame?.let { game ->
                    var buttonIconId = 0
                    var accentColor = yellow
                    Log.d("HomePage", "Last game type name: $lastGameType")
                    when (lastGameType) {
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
                    LastGameBox(
                        title = lastGameTypeName.uppercase(),
                        bgcolor = darkgray,
                        accentColor = accentColor,
                        textcolor = buttonColor,
                        onClick = {
                            Log.d("HomePage", "Last game clicked: $game")
                            navController.navigate( Screen.Game.createRoute( game.id, game.id_GameType ) )
                        },
                        icon = buttonIconId,
                        daysSinceLastPlayed = game.date
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Scoreboards",
            fontFamily = RobotoCondensed,
            fontSize = 36.sp,
            color = fontColor,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column (Modifier.padding(horizontal = 16.dp )) {
            if (gameTypes.isNotEmpty()) {
                for (gameType in gameTypes) {
                    if (gameType != null) {
                        val stats = gameStats[gameType.id] ?: GameStats()
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
                        ScoreBoardBox(
                            title = gameType.name.uppercase(),
                            description = gameType.description,
                            bgcolor = darkgray,
                            accentColor = accentColor,
                            textcolor = buttonColor,
                            onClick = { navController.navigate(Screen.SetUp.createRoute(gameType.id, accentColor)) },
                            icon = buttonIconId,
                            timesPlayed = stats.timesPlayed,
                            daysSinceLastPlayed = stats.daysSinceLastPlayed
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            } else {
                Text(
                    text = "Loading ... ",
                    fontFamily = LeagueGothic,
                    fontSize = 36.sp,
                    color = white,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Utilities",
            fontFamily = RobotoCondensed,
            fontSize = 36.sp,
            color = fontColor,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(Modifier.padding(horizontal = 16.dp)) {
            // Dice Roller Button
            UtilitiesBox(
                title = "DICE ROLLER",
                bgcolor = darkgray,
                accentColor = cream,
                textcolor = buttonColor,
                onClick = {
                    navController.navigate(Screen.Utilities.createRoute(1))
                },
                icon = R.drawable.dices,
                description = "Roll some dice and see what happens!"

            )
            Spacer(modifier = Modifier.height(16.dp))
            UtilitiesBox(
                title = "COIN TOSSER",
                bgcolor = darkgray,
                accentColor = cream,
                textcolor = buttonColor,
                onClick = {
                    navController.navigate(Screen.Utilities.createRoute(2))
                },
                icon = R.drawable.coin_toss,
                description = "Toss a coin to see if it lands on heads or tails.",
            )
            Spacer(modifier = Modifier.height(16.dp))
            UtilitiesBox(
                title = "TIMER",
                bgcolor = darkgray,
                accentColor = cream,
                textcolor = buttonColor,
                onClick = {
                    navController.navigate(Screen.Utilities.createRoute(3))
                },
                icon = R.drawable.sand_clock,
                description = "Count down timer",
            )
        }
        // --- END: ADDED UTILITIES SECTION ---
        Spacer(modifier = Modifier.height(40.dp)) // Add some space at the bottom
    }
}
