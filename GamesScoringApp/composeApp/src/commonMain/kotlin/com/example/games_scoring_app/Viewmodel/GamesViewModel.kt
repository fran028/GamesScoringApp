package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.GameStats
import com.example.games_scoring_app.Data.GameWithPlayers
import com.example.games_scoring_app.Data.Games
import com.example.games_scoring_app.Data.GamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
 import kotlin.text.padStart
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class GamesViewModel(private val gamesRepository: GamesRepository) : ViewModel() {

    private val _lastGame = MutableStateFlow<Games?>(null)
    val lastGame: StateFlow<Games?> = _lastGame

    private val _allGamesWithPlayers = MutableStateFlow<List<GameWithPlayers>>(emptyList())
    val allGamesWithPlayers: StateFlow<List<GameWithPlayers>> = _allGamesWithPlayers.asStateFlow()

    private val _currentGame = MutableStateFlow<Games?>(null)
    val currentGame: StateFlow<Games?> = _currentGame

    private val _gameStats = MutableStateFlow<Map<Int, GameStats>>(emptyMap())
    val gameStats: StateFlow<Map<Int, GameStats>> = _gameStats

    fun getLastGame() {
        viewModelScope.launch(Dispatchers.IO) {
            val games = gamesRepository.getLastGame()
            _lastGame.value = games
        }
    }

    fun getAllGamesWithPlayers() {
        viewModelScope.launch {
            gamesRepository.getAllGamesWithPlayers().collect { gamesList ->
                _allGamesWithPlayers.value = gamesList
            }
        }
    }

    suspend fun addNewGame(game: Games): Long {
        return withContext(Dispatchers.IO) {
            gamesRepository.addNewGame(game)
        }
    }

    fun getGameById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val game = gamesRepository.getGameById(id)
            _currentGame.value = game
        }
    }

    fun deleteGame(game: Games) {
        viewModelScope.launch(Dispatchers.IO) {
            gamesRepository.deleteGame(game)
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun getTodaysDate(): String {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return "${today.dayOfMonth.toString().padStart(2, '0')}/${today.monthNumber.toString().padStart(2, '0')}/${today.year}"
    }

    fun emptyGame(): Games {
        return Games(
            id_GameType = 0,
            date = getTodaysDate()
        )
    }

    fun getStatsForGameType(gameTypeId: Int) {
        viewModelScope.launch {
            val count = gamesRepository.getGamesCount(gameTypeId)
            val lastDateStr = gamesRepository.getLastPlayedDate(gameTypeId)
            val daysString = if (lastDateStr != null) {
                calculateDaysSince(lastDateStr)
            } else {
                "N/A"
            }
            _gameStats.value = _gameStats.value + (gameTypeId to GameStats(count, daysString))
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun calculateDaysSince(dateStr: String): String {
        return try {
            val parts = dateStr.split("/")
            val day = parts[0].toInt()
            val month = parts[1].toInt()
            var year = parts[2].toInt()

            if (year < 100) year += 2000

            val lastPlayedDate = LocalDate(year, month, day)
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

            // This calculates the absolute difference in days
            val days = lastPlayedDate.daysUntil(today)

            when {
                days == 0 -> "Hoy"
                days == 1 -> "Ayer"
                days < 0 -> "Fecha futura"
                else -> "Hace $days días"
            }
        } catch (e: Exception) {
            "N/A"
        }
    }
}