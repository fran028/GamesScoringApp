package com.example.games_scoring_app.Data

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.games_scoring_app.AppContext

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = AppContext.get()
    val dbFile = appContext.getDatabasePath("games.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
