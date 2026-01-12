package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.games_scoring_app.Data.ScoresRepository

class ScoresViewModelFactory( private val repository: ScoresRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoresViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoresViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}