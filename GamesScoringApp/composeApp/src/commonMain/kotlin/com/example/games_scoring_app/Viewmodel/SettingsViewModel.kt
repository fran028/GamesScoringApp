package com.example.games_scoring_app.Viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.games_scoring_app.Data.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel( private val settingsRepository: SettingsRepository ): ViewModel() {

    private val _themeMode = MutableStateFlow(0)
    val themeMode: StateFlow<Int> = _themeMode
    fun switchThemeMode() {
        Log.d("SettingsViewModel", "Switching theme mode")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("SettingsViewModel", "Updating theme mode")
            if (_themeMode.value == 0) {
                Log.d("SettingsViewModel", "Updating theme mode to 1")
                settingsRepository.updateThemeMode(1)
            } else {
                Log.d("SettingsViewModel", "Updating theme mode to 0")
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