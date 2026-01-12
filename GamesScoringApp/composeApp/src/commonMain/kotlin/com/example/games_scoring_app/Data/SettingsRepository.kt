package com.example.games_scoring_app.Data

class SettingsRepository(private val SettingsDao: SettingsDao) {

    fun insertSettings(settings: Settings) {
        SettingsDao.insertSettings(settings)
    }

    fun getSettings(): List<Settings> {
        return SettingsDao.getSettings()
    }

    fun updateThemeMode(themeMode: Int) {
        SettingsDao.updateThemeMode(themeMode)
    }

    fun getSettingByName(name: String): Settings? {
        return SettingsDao.getSettingByName(name)
    }

}