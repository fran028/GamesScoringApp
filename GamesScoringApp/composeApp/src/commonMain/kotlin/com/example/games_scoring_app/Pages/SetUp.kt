package com.example.games_scoring_app.Pages

import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.games_scoring_app.Components.ButtonBar
import com.example.games_scoring_app.Components.GameTypeTitle
import com.example.games_scoring_app.Components.IconButtonBar
import com.example.games_scoring_app.Components.LoadingMessage
import com.example.games_scoring_app.Components.PlayerAmountGrid
import com.example.games_scoring_app.Components.WidgetTitle
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.Games
import com.example.games_scoring_app.Data.GamesRepository
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.PlayersRepository
import com.example.games_scoring_app.Data.ScoreTypesRepository
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Data.ScoresRepository
import com.example.games_scoring_app.Data.SettingsRepository
import com.example.games_scoring_app.Screen
import com.example.games_scoring_app.Theme.*
import com.example.games_scoring_app.Viewmodel.GameTypesViewModel
import com.example.games_scoring_app.Viewmodel.GameTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.GamesViewModel
import com.example.games_scoring_app.Viewmodel.GamesViewModelFactory
import com.example.games_scoring_app.Viewmodel.PlayersViewModel
import com.example.games_scoring_app.Viewmodel.PlayersViewModelFactory
import com.example.games_scoring_app.Viewmodel.ScoreTypesViewModel
import com.example.games_scoring_app.Viewmodel.ScoreTypesViewModelFactory
import com.example.games_scoring_app.Viewmodel.ScoresViewModel
import com.example.games_scoring_app.Viewmodel.ScoresViewModelFactory
import com.example.games_scoring_app.Viewmodel.SettingsViewModel
import com.example.games_scoring_app.Viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import org.jetbrains.compose.resources.painterResource
import gamesscoringapp.composeapp.generated.resources.*

@Composable
fun SetupPage(navController: NavController, gameType: Int, gameColorHex: String, existingPlayerNames: String?) {
    val TAG  = "SetupPage"
    val scrollState = rememberScrollState()

    val gameColor = remember(gameColorHex) {
        try {
            Color(gameColorHex.toLong(16).toInt())

        } catch (e: Exception) {

            red
        }
    }

    val applicationScope = CoroutineScope(SupervisorJob())
    val database = remember { AppDatabase.getDatabase(applicationScope) }

    val coroutineScope = rememberCoroutineScope()

    // --- ViewModel Setup ---
    val gamesRepository = remember { GamesRepository(database.gamesDao()) }
    val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModelFactory(gamesRepository))
    val gameTypesRepository = remember { GameTypesRepository(database.gameTypesDao()) }
    val gameTypesViewModel: GameTypesViewModel = viewModel(factory = GameTypesViewModelFactory(gameTypesRepository))
    val scoreTypesRepository = remember { ScoreTypesRepository(database.scoreTypesDao()) }
    val scoreTypesViewModel: ScoreTypesViewModel = viewModel(factory = ScoreTypesViewModelFactory(scoreTypesRepository))
    val playersRepository = remember { PlayersRepository(database.playersDao()) }
    val playersViewModel: PlayersViewModel = viewModel(factory = PlayersViewModelFactory(playersRepository))
    val scoresRepository = remember { ScoresRepository(database.scoresDao()) }
    val scoresViewModel: ScoresViewModel = viewModel(factory = ScoresViewModelFactory(scoresRepository))
    val settingsRepository = remember { SettingsRepository(database.settingsDao()) }
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModelFactory(settingsRepository))

    // --- State Variables ---
    val themeMode by settingsViewModel.themeMode.collectAsState()
    val gameTypes by gameTypesViewModel.allGameTypes.collectAsState()

    val names = remember { mutableStateListOf<String>() }
    val thisGameType = remember { mutableStateOf(gameTypesViewModel.emptyGameType()) }
    val maxPlayers by remember { derivedStateOf { thisGameType.value.maxPlayers } }
    val minPlayers by remember { derivedStateOf { thisGameType.value.minPlayers } }
    var selectedPlayerCount by remember { mutableStateOf(minPlayers) }
    val defaultNames = listOf("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9", "P10")
    val showSetup = remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(key1 = Unit) {
        gameTypesViewModel.getAllGameTypes()
        settingsViewModel.getThemeMode()
    }

    LaunchedEffect(key1 = gameTypes, key2 = minPlayers) {
        if (gameTypes.isNotEmpty()) {

            val foundGameType = gameTypes.find { it?.id == gameType }

            if (foundGameType != null) {
                thisGameType.value = foundGameType // Update the State

                val initialPlayerNames = if (!existingPlayerNames.isNullOrEmpty()) {
                    existingPlayerNames.split(",")
                } else {
                    null
                }

                selectedPlayerCount = initialPlayerNames?.size ?: minPlayers


                names.clear()
                // Initialize the names list with empty strings up to the max player count
                repeat(foundGameType.maxPlayers) { index ->
                    // If we have an existing name for this index, use it. Otherwise, use an empty string.
                    val name = initialPlayerNames?.getOrNull(index) ?: ""
                    names.add(name)
                }
                kotlinx.coroutines.delay(1000) // Consider if this delay is necessary
                showSetup.value = true
            }
        }
    }

    val backgroundColor = if (themeMode == 0) black else white
    val fontColor = if (themeMode == 0) white else black
    val buttonColor = if (themeMode == 0) white else black
    val buttonFontColor = if (themeMode == 0) black else white

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if(showSetup.value) {
            Spacer(modifier = Modifier.height(64.dp))
            WidgetTitle("GAME SETUP", Res.drawable.games_retro, navController)
            Spacer(modifier = Modifier.height(28.dp))
            val buttonIconId = when (thisGameType.value.type) {
                "Dados" -> Res.drawable.dices
                "Cartas" -> Res.drawable.card
                "Generico" -> Res.drawable.paper
                else -> Res.drawable.paper
            }
            Column (Modifier.padding(horizontal = 16.dp )) {
                GameTypeTitle(
                    title = thisGameType.value.name.uppercase(),
                    bgcolor = darkgray,
                    textcolor = fontColor,
                    icon = buttonIconId,
                    accentColor = gameColor,
                )
            }
            Column (Modifier.padding(horizontal = 16.dp )) {
                if (thisGameType.value.name != "Truco") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Player Amount",
                        fontFamily = LeagueGothic,
                        fontSize = 48.sp,
                        color = fontColor,
                        modifier = Modifier
                            // Give it padding so it's not at the very edge of the box
                            .padding( vertical = 4.dp)
                            .fillMaxWidth(), // Make text take full width to apply alignment
                        textAlign = TextAlign.Left
                    )

                    PlayerAmountGrid(
                        maxPlayers = maxPlayers,
                        minPlayers = minPlayers,
                        selectedAmount = selectedPlayerCount,
                        onPlayerAmountSelected = { amount ->
                            selectedPlayerCount = amount
                        },
                        selectedbgcolor = gameColor,
                        bgcolor = backgroundColor, // Unselected should be the main background
                        textcolor = fontColor      // Text for unselected should be main font color
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = if (thisGameType.value.name == "Truco") "Team Names" else "Player Names",
                    fontFamily = LeagueGothic,
                    fontSize = 48.sp,
                    color = fontColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column(
                    modifier = Modifier  // Set padding for the box itself
                        .background(darkgray, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                ) {

                    Spacer(modifier = Modifier.height(12.dp))
                    //... inside the Column, inside the for loop

                    for (i in 0 until maxPlayers) {
                        val isSelected = i < selectedPlayerCount
                        val isNextAvailable = i == selectedPlayerCount // The "Add Player..." slot

                        var inputcolor = buttonColor
                        if (isSelected && thisGameType.value.name != "Truco") {
                            inputcolor = gameColor
                        }

                        // Use isNextAvailable as a key for pointerInput so it resets when counts change
                        val modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .pointerInput(isNextAvailable, selectedPlayerCount) {
                                if (isNextAvailable) {
                                    detectTapGestures {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        selectedPlayerCount++
                                    }
                                }
                            }

                        val border = if (isSelected) {
                            BorderStroke(4.dp, inputcolor)
                        } else {
                            if (isNextAvailable) {
                                BorderStroke(width = 2.dp, color = gray)
                            } else {
                                BorderStroke(2.dp, gray.copy(alpha = 0.5f))
                            }
                        }
                        Box(modifier = modifier) {
                            TextField(
                                value = names[i],
                                onValueChange = { names[i] = it },
                                enabled = isSelected,
                                placeholder = {
                                    val placeholderText = if (isNextAvailable) "Add Player..." else "Player Name"
                                    Text(
                                        text = placeholderText,
                                        style = TextStyle(
                                            color = fontColor.copy(alpha = 0.5f),
                                            fontSize = 24.sp,
                                            fontFamily = LeagueGothic
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxSize() // Fill the Box
                                    .onFocusChanged { focusState ->
                                        if (focusState.isFocused) {
                                            haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                        }
                                    }
                                    .border(border, shape = RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = TextStyle(
                                    fontFamily = LeagueGothic,
                                    fontSize = 32.sp,
                                    color = fontColor
                                ),
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = black,
                                    focusedContainerColor = black,
                                    disabledContainerColor = if (isNextAvailable) darkgray else black.copy(alpha = 0.3f),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    cursorColor = fontColor
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                        }

                        if (thisGameType.value.name != "Truco") {
                            Spacer(modifier = Modifier.height(6.dp))
                        } else {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                ButtonBar(
                    text = "START GAME",
                    bgcolor = green,
                    height = 48.dp,
                    textcolor = white,
                    onClick = {

                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        coroutineScope.launch {
                            // FIXED: Corrected logic to match new architecture
                            // 1. Create the new game object and insert it to get its ID.
                            val gameToInsert = Games(
                                id_GameType = thisGameType.value.id,
                            )
                            val gameId = gamesViewModel.addNewGame(gameToInsert)

                            // 2. Get the required ScoreTypes for this game.
                            val scoreTypesForGame =
                                scoreTypesViewModel.getScoreTypesForGame(thisGameType.value.id)

                            // 3. Iterate through players to insert them and their initial scores.
                            for (i in 0 until selectedPlayerCount) {
                                val playerName =
                                    if (names[i].isNotBlank()) names[i] else defaultNames[i]

                                // 3a. Create and insert the player to get their ID.
                                val newPlayer = Players(
                                    id_game = gameId.toInt(),
                                    name = playerName
                                )
                                val newPlayerId = playersViewModel.addNewPlayer(newPlayer)

                                // 3b. CRITICAL: Create an initial score entry (usually 0) for each required score type.
                                scoreTypesForGame.forEach { scoreType ->
                                    val initialScore = Scores(
                                        id_player = newPlayerId.toInt(),
                                        id_score_type = scoreType.id,
                                        score = 0f,
                                        isFinalScore = false
                                    )
                                    scoresViewModel.addScore(initialScore)
                                }
                            }
                            navController.navigate(
                                Screen.Game.createRoute(
                                    gameId = gameId.toInt(),
                                    gameTypeId = thisGameType.value.id
                                )
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

        } else {
            LoadingMessage(wheelColor = fontColor, themeMode = themeMode)
        }
    }
}
