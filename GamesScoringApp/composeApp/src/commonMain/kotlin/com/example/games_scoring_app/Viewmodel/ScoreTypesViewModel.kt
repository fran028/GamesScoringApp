package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.ScoreTypes
import com.example.games_scoring_app.Data.ScoreTypesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScoreTypesViewModel(private val repository: ScoreTypesRepository) :
    ViewModel() {
    private val _scoreTypesForGame = MutableStateFlow<List<ScoreTypes>>(emptyList())
    val scoreTypesForGame: StateFlow<List<ScoreTypes>> = _scoreTypesForGame

    fun getScoreTypesByGameTypeId(gameTypeId: Int) {
        viewModelScope.launch {
            repository.getScoreTypesByGameTypeId(gameTypeId).collect {
                _scoreTypesForGame.value = it
            }
        }
    }

    /**
     * NEW: Fetches the list of score types for a specific game type ID.
     * This is a one-shot suspend function that calls the repository.
     */
    suspend fun getScoreTypesForGame(gameTypeId: Int): List<ScoreTypes> {
        return repository.getScoreTypesByGameTypeIdAsList(gameTypeId)
    }
}
