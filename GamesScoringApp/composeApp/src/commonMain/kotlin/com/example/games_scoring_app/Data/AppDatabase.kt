package com.example.games_scoring_app.Data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

// These tell the compiler that the platform-specific code exists elsewhere
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

// Suppression is necessary because Room generates the 'actual' implementation automatically
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

@Database(
    entities = [Players::class, Games::class, Scores::class, GameTypes::class, Settings::class, ScoreTypes::class],
    version = 5,
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playersDao(): PlayersDao
    abstract fun gamesDao(): GamesDao
    abstract fun scoresDao(): ScoresDao
    abstract fun gameTypesDao(): GameTypesDao
    abstract fun settingsDao(): SettingsDao
    abstract fun scoreTypesDao(): ScoreTypesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val _isDatabaseReady = MutableStateFlow(false)
        val isDatabaseReady: StateFlow<Boolean> = _isDatabaseReady.asStateFlow()

        fun getDatabase(scope: CoroutineScope): AppDatabase {
            // Replaced 'synchronized' with a simple null-check pattern compatible with KMP
            val existingInstance = INSTANCE
            if (existingInstance != null) return existingInstance

            val instance = getDatabaseBuilder()
                .setDriver(BundledSQLiteDriver())
                .fallbackToDestructiveMigration(dropAllTables = true)
                .addCallback(AppDatabaseCallback(scope))
                .build()

            INSTANCE = instance
            signalDatabaseOperational()
            return instance
        }

        private fun signalDatabaseOperational() {
            if (!_isDatabaseReady.value) {
                _isDatabaseReady.value = true
            }
        }

        private fun stopSignalDatabaseOperational() {
            _isDatabaseReady.value = false
        }
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(connection: SQLiteConnection) {
            super.onOpen(connection)
            scope.launch(Dispatchers.IO) {
                val database = INSTANCE ?: return@launch
                stopSignalDatabaseOperational()
                populateDatabase(database.gameTypesDao(), database.settingsDao(), database.scoreTypesDao())
                signalDatabaseOperational()
            }
        }

        suspend fun populateDatabase(gameTypesDao: GameTypesDao, settingsDao: SettingsDao, scoreTypesDao: ScoreTypesDao) {
            if (gameTypesDao.getAllGameTypesAsList().isEmpty()) {
                val settings = settingsDao.getSettings()
                if (settings.none { it.name == "theme" }) {
                    settingsDao.insertSettings(Settings(name = "theme", value = 0))
                }

                val trucoId = gameTypesDao.insertGameType(GameTypes(name = "Truco", description = "Juego de cartas popular en Argentina.", maxPlayers = 2, minPlayers = 2, maxScore = 30, type = "Cartas"))
                val generalaId = gameTypesDao.insertGameType(GameTypes(name = "Generala", description = "Juego de dados.", maxPlayers = 8, minPlayers = 1, maxScore = 370, type = "Dados"))
                val pointsId = gameTypesDao.insertGameType(GameTypes(name = "Points", description = "Contar puntos hasta un objetivo.", maxPlayers = 8, minPlayers = 2, maxScore = 1000, type = "Generico"))
                val rankingId = gameTypesDao.insertGameType(GameTypes(name = "Ranking", description = "Rankea el resultado.", maxPlayers = 8, minPlayers = 3, maxScore = 0, type = "Generico"))
                val levelsId = gameTypesDao.insertGameType(GameTypes(name = "Levels", description = "Avanza niveles.", maxPlayers = 8, minPlayers = 1, maxScore = 0, type = "Generico"))

                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = trucoId.toInt(), name = "Final Score"))

                listOf("1", "2", "3", "4", "5", "6", "Escalera", "Full", "Poker", "Generala", "Generala x2").forEach {
                    scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = generalaId.toInt(), name = it))
                }

                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = pointsId.toInt(), name = "Final Score"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = rankingId.toInt(), name = "Final Score"))
                scoreTypesDao.insertScoreType(ScoreTypes(id_game_type = levelsId.toInt(), name = "Final Score"))
            }
        }
    }
}