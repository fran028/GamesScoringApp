package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.games_scoring_app.Data.ScoresRepository
import kotlin.reflect.KClass

class ScoresViewModelFactory(private val repository: ScoresRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when {
            modelClass == ScoresViewModel::class -> {
                ScoresViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }
    }
}