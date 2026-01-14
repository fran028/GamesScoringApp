package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.PlayerWithScores
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.PlayersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO // Added this explicit import
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayersViewModel(private val playersRepository: PlayersRepository) : ViewModel() {

    /**
     * MODIFIED: This is now a suspend function that returns the ID of the new player.
     */
    suspend fun addNewPlayer(player: Players): Long {
        return withContext(Dispatchers.IO) {
            playersRepository.insertPlayer(player)
        }
    }

    /**
     * NEW: Returns a Flow that emits a list of players, each with their associated scores.
     */
    fun getPlayersWithScores(gameId: Int): Flow<List<PlayerWithScores>> {
        return playersRepository.getPlayersWithScores(gameId)
    }

    /**
     * Updates an existing player in the database.
     */
    suspend fun updatePlayer(player: Players) {
        withContext(Dispatchers.IO) {
            playersRepository.updatePlayer(player)
        }
    }

    /**
     * Fetches the player list without their scores.
     */
    suspend fun fetchPlayersByGameId(id: Int): List<Players> {
        return playersRepository.getPlayersByGameIdAsList(id)
    }
}