package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.GameTypes
import com.example.games_scoring_app.Data.GameTypesRepository
import com.example.games_scoring_app.Data.Games
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameTypesViewModel(private val gameTypesRepository: GameTypesRepository) : ViewModel() {
    fun insertGameType(gameType: GameTypes) {
        viewModelScope.launch(Dispatchers.IO) {
            gameTypesRepository.insertGameType(gameType)
        }
    }

    private val _allGameTypes = MutableStateFlow<List<GameTypes?>>(listOf())
    val allGameTypes: StateFlow<List<GameTypes?>> = _allGameTypes

    private val _gameType = MutableStateFlow<GameTypes?>(null)
    val gameType: StateFlow<GameTypes?> = _gameType


    fun getAllGameTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            // Collect the values from the Flow
            gameTypesRepository.getAllGameTypes().collect { gamesTypeList ->
                // Update the StateFlow on the main thread with the emitted list
                withContext(Dispatchers.Main) {
                    _allGameTypes.value = gamesTypeList
                }
            }
        }
    }

    fun getGameTypeById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = gameTypesRepository.getGameTypeById(id)
            // Update the StateFlow on the main thread
            withContext(Dispatchers.Main) {
                _gameType.value = result
            }
        }
    }

    // This suspend function can be kept if needed elsewhere, but the one above is what Game.kt will use.
    suspend fun fetchGameTypeById(id: Int): GameTypes? {
        return gameTypesRepository.getGameTypeById(id)
    }

    fun emptyGameType(): GameTypes {
        return GameTypes(
            id = 0,
            name = "Empty Game Type",
            maxPlayers = 8,
            minPlayers = 0,
            maxScore = 0
        )
    }

    fun getGameTypeNameById(id: Int): String? {
        // This function is inefficient. It's better to fetch directly from the repository.
        // But if you keep it, ensure getAllGameTypes() has been called first.
        val gameType = allGameTypes.value.find { it?.id == id }
        return gameType?.name
    }
}
