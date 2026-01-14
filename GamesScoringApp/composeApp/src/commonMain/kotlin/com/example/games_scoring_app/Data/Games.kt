package com.example.games_scoring_app.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.text.padStart
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "games")
data class Games(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_GameType") var id_GameType: Int,
    @ColumnInfo(name = "date") val date: String = getTodaysDate(),
)

/**
 * Helper function to get the current date as a formatted string.
 * This version is Kotlin Multiplatform compatible.
 */
@OptIn(ExperimentalTime::class)
private fun getTodaysDate(): String {
    // Clock.System is the standard KMP way to get the current time
    val now = Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    // Format: dd/MM/yyyy
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val year = localDateTime.year

    return "$day/$month/$year"
}