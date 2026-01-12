package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.Players
import com.example.games_scoring_app.Data.Scores
import com.example.games_scoring_app.Data.ScoresRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScoresViewModel(private val scoresRepository: ScoresRepository) : ViewModel() {
    fun addNewScore(score: Scores) {
        viewModelScope.launch(Dispatchers.IO) {
            scoresRepository.insertScore(score)
        }
    }


    suspend fun getPlayerScore(playerId: Int): Scores {  // Modified to suspend function
        return withContext(Dispatchers.IO) {   // Run on Dispatchers.IO
            scoresRepository.getScoresByPlayerId(playerId)
        }
    }

    private val _allPlayersScores = MutableStateFlow<List<Scores>>(emptyList())
    val allPlayersScores: StateFlow<List<Scores>> = _allPlayersScores

    fun getAllPlayersScores(players: List<Players?>){
        viewModelScope.launch(Dispatchers.IO) {
            val scores = mutableListOf<Scores>()
            for (player in players) {
                val score = scoresRepository.getScoresByPlayerId(player!!.id)
                scores.add(score)
            }
            _allPlayersScores.value = scores
        }
    }

    fun addEmtpyScoreToAllPlayers(players: List<Players?>){
        viewModelScope.launch(Dispatchers.IO) {
            for (player in players) {
                val newScore = Scores(id_player = player!!.id, score = 0, isFinalScore = false, id_score_type = 0)
                addNewScore(newScore)
            }
        }
    }

    suspend fun deleteScore(score: Scores) {
        scoresRepository.deleteScore(score)
    }



    fun getAllScores(): List<Scores> {
        return scoresRepository.getAllScores()
    }

    fun getScoreByGameId(gameId: Int): Scores? {
        return scoresRepository.getScoreById(gameId)
    }

    /**
     * NEW: The function that SetUp.kt needs.
     * It takes a Scores object and uses viewModelScope to launch a coroutine
     * that calls the repository to insert it into the database.
     */
    fun addScore(score: Scores) {
        viewModelScope.launch {
            scoresRepository.insertScore(score)
        }
    }

    /**
     * The function that the game scoreboards will use to update scores.
     */
    fun updateScore(score: Scores) {
        viewModelScope.launch {
            scoresRepository.updateScore(score)
        }
    }
}