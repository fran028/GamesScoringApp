package com.example.games_scoring_app.Data

import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.games_scoring_app.AppContext
import androidx.room.RoomDatabaseConstructor

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = AppContext.get()
    val dbFile = appContext.getDatabasePath("games.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
