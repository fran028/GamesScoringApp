package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.games_scoring_app.Data.GamesRepository

class GamesViewModelFactory(private val repository: GamesRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GamesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GamesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}