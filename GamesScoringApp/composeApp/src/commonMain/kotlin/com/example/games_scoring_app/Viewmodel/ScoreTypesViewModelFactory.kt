package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.games_scoring_app.Data.ScoreTypesRepository

/**
 * Factory for creating a ScoreTypesViewModel with a constructor that takes a ScoreTypesRepository.
 */
class ScoreTypesViewModelFactory(private val repository: ScoreTypesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreTypesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoreTypesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
