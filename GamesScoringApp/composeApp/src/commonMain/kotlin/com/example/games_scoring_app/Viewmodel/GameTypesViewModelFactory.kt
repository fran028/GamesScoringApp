package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.games_scoring_app.Data.GameTypesRepository

class GameTypesViewModelFactory(private val repository: GameTypesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameTypesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameTypesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}