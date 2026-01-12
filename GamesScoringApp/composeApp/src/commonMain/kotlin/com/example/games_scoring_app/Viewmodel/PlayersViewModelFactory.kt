package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.games_scoring_app.Data.PlayersRepository

class PlayersViewModelFactory(private val repository: PlayersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}