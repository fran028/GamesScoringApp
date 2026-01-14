package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.GameTypes
import com.example.games_scoring_app.Data.GameTypesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameTypesViewModel(private val gameTypesRepository: GameTypesRepository) : ViewModel() {

    private val _allGameTypes = MutableStateFlow<List<GameTypes?>>(listOf())
    val allGameTypes: StateFlow<List<GameTypes?>> = _allGameTypes.asStateFlow()

    private val _gameType = MutableStateFlow<GameTypes?>(null)
    val gameType: StateFlow<GameTypes?> = _gameType.asStateFlow()

    fun insertGameType(gameType: GameTypes) {
        viewModelScope.launch(Dispatchers.IO) {
            gameTypesRepository.insertGameType(gameType)
        }
    }

    fun getAllGameTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            // In KMP, Room Flows are already off-main-thread
            gameTypesRepository.getAllGameTypes().collect { gamesTypeList ->
                _allGameTypes.value = gamesTypeList
            }
        }
    }

    fun getGameTypeById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = gameTypesRepository.getGameTypeById(id)
            _gameType.value = result
        }
    }

    // Standard suspend function for direct calls
    suspend fun fetchGameTypeById(id: Int): GameTypes? {
        return gameTypesRepository.getGameTypeById(id)
    }

    fun emptyGameType(): GameTypes {
        return GameTypes(
            id = 0,
            name = "Empty Game Type",
            maxPlayers = 8,
            minPlayers = 0,
            maxScore = 0,
            type = "Generico",
        )
    }

    fun getGameTypeNameById(id: Int): String? {
        val gameType = allGameTypes.value.find { it?.id == id }
        return gameType?.name
    }
}