package com.example.games_scoring_app.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel( private val settingsRepository: SettingsRepository ): ViewModel() {

    private val _themeMode = MutableStateFlow(0)
    val themeMode: StateFlow<Int> = _themeMode
    fun switchThemeMode() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_themeMode.value == 0) {
                settingsRepository.updateThemeMode(1)
            } else {
                settingsRepository.updateThemeMode(0)
            }
            getThemeMode()
        }
    }

    fun getThemeMode() {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = settingsRepository.getSettingByName("theme")
            if (settings != null) {
                _themeMode.value = settings.value
            }
        }
    }

}