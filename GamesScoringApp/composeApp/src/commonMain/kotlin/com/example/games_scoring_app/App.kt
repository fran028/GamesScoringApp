package com.example.games_scoring_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// CORRECT Multiplatform Navigation Imports
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Multiplatform Resource Imports
import org.jetbrains.compose.resources.painterResource
import gamesscoringapp.composeapp.generated.resources.Res
import gamesscoringapp.composeapp.generated.resources.logobig

// Your shared project imports
import com.example.games_scoring_app.Data.AppDatabase
import com.example.games_scoring_app.Pages.*
import com.example.games_scoring_app.Theme.*

@Composable
fun App() {
    // 1. Create a scope that lives with the App
    val scope = rememberCoroutineScope()

    // 2. Observe the readiness state
    // Make sure you import: androidx.compose.runtime.collectAsState
    val isReady by AppDatabase.isDatabaseReady.collectAsState()

    // 3. START the database initialization
    LaunchedEffect(Unit) {
        AppDatabase.getDatabase(scope)
    }

    Games_Scoring_AppTheme {
        if (isReady) {
            MainScreen()
        } else {
            LoadingScreen()
        }
    }
}

@Composable
fun LoadingScreen() {
    Box( // Simplified: removed long androidx.compose.foundation... prefixes
        modifier = Modifier.fillMaxSize().background(black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.logobig),
                contentDescription = "App Image",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "LOADING",
                style = TextStyle(
                    fontFamily = LeagueGothic,
                    fontSize = 60.sp,
                    color = white
                ),
                textAlign = TextAlign.Center
            )
            CircularProgressIndicator(color = white)
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // Note: WindowInsets(0,0,0,0) is also part of foundation layout,
        // which is already imported.
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            composable(Screen.Home.route) {
                HomePage(navController = navController)
            }

            composable(
                route = Screen.Game.route,
                arguments = listOf(
                    navArgument("gameId") { type = NavType.IntType },
                    navArgument("gameTypeId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
                val gameTypeId = backStackEntry.arguments?.getInt("gameTypeId") ?: 0
                GamePage(navController = navController, gameId = gameId, gameTypeId = gameTypeId)
            }

            composable(
                route = Screen.SetUp.route,
                arguments = listOf(
                    navArgument("gameType") { type = NavType.IntType },
                    navArgument("gameColor") { type = NavType.StringType },
                    navArgument("playerNames") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val gameType = backStackEntry.arguments?.getInt("gameType") ?: 0
                 val colorHex = backStackEntry.arguments?.getString("gameColor") ?: "ff0000ff"
                val playerNamesString = backStackEntry.arguments?.getString("playerNames")
                SetupPage(
                    navController = navController,
                    gameType = gameType,
                    gameColorHex = colorHex,
                    existingPlayerNames = playerNamesString
                )
            }

            composable(Screen.SavedGames.route) {
                SavedGamesPage(navController = navController)
            }

            composable(Screen.Settings.route) {
                SettingsPage(navController = navController)
            }

            composable(
                route = Screen.Utilities.route,
                arguments = listOf(
                    navArgument("utilityId") { type = NavType.IntType },
                )
            ) { backStackEntry ->
                val utilityId = backStackEntry.arguments?.getInt("utilityId") ?: 0
                UtilitiesPage(navController = navController, utilityId = utilityId)
            }
        }
    }
}