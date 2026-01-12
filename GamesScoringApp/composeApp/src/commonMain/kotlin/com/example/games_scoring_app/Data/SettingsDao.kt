package com.example.games_scoring_app.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSettings(settings: Settings)

    @Query("SELECT * FROM settings")
    fun getSettings(): List<Settings>

    @Query("UPDATE settings SET value = :themeMode WHERE name = 'theme'")
    fun updateThemeMode(themeMode: Int)

    @Query("DELETE FROM settings")
    suspend fun deleteGamesType()

    @Query("SELECT * FROM settings WHERE name = :name")
    fun getSettingByName(name: String): Settings?

}