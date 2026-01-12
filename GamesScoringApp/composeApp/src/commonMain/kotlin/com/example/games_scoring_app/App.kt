package com.example.games_scoring_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.blue
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
import com.example.games_scoring_app.ui.theme.Games_Scoring_AppTheme

@androidx.compose.runtime.Composable
fun App() {
    Games_Scoring_AppTheme {
        // Database state comes from your shared Data module
        val isDatabaseReady by AppDatabase.isDatabaseReady.collectAsState()

        if (isDatabaseReady) {
            MainScreen()
        } else {
            LoadingScreen()
        }
    }
}

@androidx.compose.runtime.Composable
fun LoadingScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize().background(black),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(Res.drawable.logobig),
                contentDescription = "App Image",
                modifier = Modifier.size(100.dp)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(10.dp))
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

@androidx.compose.runtime.Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
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
                val hex = backStackEntry.arguments?.getString("gameColor") ?: blue.value.toString(16)
                val color = Color(hex.toULong(16))
                val playerNamesString = backStackEntry.arguments?.getString("playerNames")
                SetupPage(
                    navController = navController,
                    gameType = gameType,
                    gameColor = color,
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
