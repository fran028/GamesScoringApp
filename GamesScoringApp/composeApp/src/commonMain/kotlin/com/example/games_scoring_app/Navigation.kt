package com.example.games_scoring_app

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Home : Screen("Home")
    @Serializable
    object Game : Screen("Game/{gameId}/{gameTypeId}"){
        fun createRoute(
            gameId: Int,
            gameTypeId: Int
        ): String {
            return "Game/$gameId/$gameTypeId"
        }
    }
    @Serializable
    object SetUp : Screen("SetUp/{gameType}/{gameColor}?playerNames={playerNames}"){
        fun createRoute(gameType: Int?, gameColor: Color) = "SetUp/$gameType/${gameColor.value.toString(16)}"
        fun createRouteWithPlayers(gameType: Int, gameColor: Color, playerNames: List<String>): String {
            val names = playerNames.joinToString(",")
            return "SetUp/$gameType/${gameColor.value.toString(16)}?playerNames=$names"
        }
    }
    @Serializable
    object SavedGames : Screen("SavedGames")
    @Serializable
    object Settings : Screen("Settings")
    @Serializable
    object Utilities : Screen("Utilities/{utilityId}"){
        fun createRoute(utilityId: Int) = "Utilities/$utilityId"
    }

}
