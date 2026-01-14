package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.games_scoring_app.Data.SettingsRepository
import kotlin.reflect.KClass

class SettingsViewModelFactory(private val repository: SettingsRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when {
            modelClass == SettingsViewModel::class -> {
                SettingsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }
    }
}