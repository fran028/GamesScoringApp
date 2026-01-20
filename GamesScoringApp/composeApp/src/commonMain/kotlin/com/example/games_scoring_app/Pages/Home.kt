package com.example.games_scoring_app.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.jetbrains.compose.resources.stringResource
import gamesscoringapp.composeapp.generated.resources.*
import com.example.games_scoring_app.Components.*
import com.example.games_scoring_app.Data.*
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.*
import com.example.games_scoring_app.Viewmodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.text.toString
import kotlin.text.toUInt

@Composable
fun HomePage(navController: NavController) {


    val haptic = LocalHapticFeedback.current

    // KMP String resource syntax
    val appName = stringResource(Res.string.app_name)
    val scrollState = rememberScrollState()

    val applicationScope = remember { CoroutineScope(SupervisorJob()) }
    // LocalContext is Android-only. KMP Database doesn't need context anymore.
    val database = remember { AppDatabase.getDatabase(applicationScope) }

    val gamesRepository = remember { GamesRepository(database.gamesDao()) }
    val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModelFactory(gamesRepository))

    val gameTypesRepository = remember { GameTypesRepository(database.gameTypesDao()) }
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = GameTypesViewModelFactory(gameTypesRepository))

    val settingsRepository = remember { SettingsRepository(database.settingsDao()) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsRepository))

    val themeMode by settingsViewModel.themeMode.collectAsState()
    val lastGame by gamesViewModel.lastGame.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()
    val gameStats by gamesViewModel.gameStats.collectAsState()

    var lastGameTypeName by remember { mutableStateOf("") }
    var lastGameType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        gamesViewModel.getLastGame()
        gameTypesViewModel.getAllGameTypes()
        settingsViewModel.getThemeMode()
    }

    LaunchedEffect(lastGame, gameTypes) {
        lastGame?.let { game ->
            val type = gameTypes.find { it?.id == game.id_GameType }
            lastGameTypeName = type?.name ?: ""
            lastGameType = type?.type ?: ""
        }
    }

    LaunchedEffect(gameTypes) {
        gameTypes.forEach { gameType ->
            gameType?.let { gamesViewModel.getStatsForGameType(it.id) }
        }
    }

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Corrected WidgetTitle call
        WidgetTitle(appName.uppercase(), Res.drawable.game_topview, navController)

        if(lastGame != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Last Game",
                fontFamily = RobotoCondensed,
                fontSize = 36.sp,
                color = fontColor,
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(Modifier.padding(horizontal = 16.dp)) {
                lastGame?.let { game ->
                    // Map type to KMP DrawableResource
                    val (buttonIcon, accentColor) = when (lastGameType) {
                        "Dados" -> Res.drawable.dices to blue
                        "Cartas" -> Res.drawable.card to yellow
                        "Generico" -> Res.drawable.paper to green
                        else -> Res.drawable.paper to white
                    }

                    LastGameBox(
                        title = lastGameTypeName.uppercase(),
                        bgcolor = darkgray,
                        accentColor = accentColor,
                        textcolor = buttonColor,
                        onClick = {

                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            navController.navigate(Screen.Game.createRoute(game.id, game.id_GameType))
                        },
                        icon = buttonIcon,
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
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            if (gameTypes.isNotEmpty()) {
                gameTypes.forEach { gameType ->
                    gameType?.let { type ->
                        val stats = gameStats[type.id] ?: GameStats()
                        val (buttonIcon, accentColor) = when (type.type) {
                            "Dados" -> Res.drawable.dices to blue
                            "Cartas" -> Res.drawable.card to yellow
                            "Generico" -> Res.drawable.paper to green
                            else -> Res.drawable.paper to white
                        }

                        ScoreBoardBox(
                            title = type.name.uppercase(),
                            description = type.description,
                            bgcolor = darkgray,
                            accentColor = accentColor,
                            textcolor = buttonColor,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                navController.navigate(Screen.SetUp.createRoute(type.id, accentColor))
                            },
                            icon = buttonIcon,
                            timesPlayed = stats.timesPlayed,
                            daysSinceLastPlayed = stats.daysSinceLastPlayed
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            } else {
                Text("Loading...", color = fontColor, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Utilities",
            fontFamily = RobotoCondensed,
            fontSize = 36.sp,
            color = fontColor,
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            UtilitiesBox(
                title = "DICE ROLLER",
                bgcolor = darkgray,
                accentColor = cream,
                textcolor = buttonColor,
                onClick = {

                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navController.navigate(Screen.Utilities.createRoute(1)) },
                icon = Res.drawable.dices,
                description = "Roll some dice and see what happens!"
            )
            Spacer(modifier = Modifier.height(16.dp))
            UtilitiesBox(
                title = "COIN TOSSER",
                bgcolor = darkgray,
                accentColor = cream,
                textcolor = buttonColor,
                onClick = {

                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navController.navigate(Screen.Utilities.createRoute(2)) },
                icon = Res.drawable.coin_toss,
                description = "Toss a coin to see if it lands on heads or tails."
            )
            Spacer(modifier = Modifier.height(16.dp))
            UtilitiesBox(
                title = "TIMER",
                bgcolor = darkgray,
                accentColor = cream,
                textcolor = buttonColor,
                onClick = {

                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    navController.navigate(Screen.Utilities.createRoute(3)) },
                icon = Res.drawable.sand_clock,
                description = "Count down timer"
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}