package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.games_scoring_app.Data.ScoreTypesRepository
import kotlin.reflect.KClass

/**
 * Factory for creating a ScoreTypesViewModel with a constructor that takes a ScoreTypesRepository.
 */
class ScoreTypesViewModelFactory(private val repository: ScoreTypesRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when {
            modelClass == ScoreTypesViewModel::class -> {
                ScoreTypesViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }
    }
}