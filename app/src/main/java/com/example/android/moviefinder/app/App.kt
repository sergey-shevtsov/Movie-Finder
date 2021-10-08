package com.example.android.moviefinder.app

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.moviefinder.model.local.FavoritesDao
import com.example.android.moviefinder.model.local.FavoritesDatabase
import com.example.android.moviefinder.model.local.HistoryDao
import com.example.android.moviefinder.model.local.HistoryDatabase

class App : Application() {

    companion object {
        private var appInstance: App? = null
        private var hdb: HistoryDatabase? = null
        private var fdb: FavoritesDatabase? = null
        private const val HISTORY_DB_NAME = "History.db"
        private const val FAVORITES_DB_NAME = "Favorites.db"

        fun getHistoryDao(): HistoryDao {
            if (hdb == null) {
                synchronized(HistoryDatabase::class.java) {
                    if (appInstance == null)
                        throw IllegalStateException("Application is null while creating DataBase")
                    hdb = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        HistoryDatabase::class.java,
                        HISTORY_DB_NAME
                    ).addMigrations(HISTORY_MIGRATION_1_2, HISTORY_MIGRATION_2_3).build()
                }
            }

            return hdb!!.historyDao()
        }

        fun getFavoritesDao(): FavoritesDao {
            if (fdb == null) {
                synchronized(FavoritesDatabase::class.java) {
                    if (appInstance == null)
                        throw IllegalStateException("Application is null while creating DataBase")
                    fdb = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        FavoritesDatabase::class.java,
                        FAVORITES_DB_NAME
                    ).build()
                }
            }

            return fdb!!.favoritesDao()
        }

        private val HISTORY_MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE HistoryEntity")
                database.execSQL("CREATE TABLE HistoryEntity (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "movieId INTEGER NOT NULL," +
                        "title TEXT NOT NULL," +
                        "releasedYear TEXT NOT NULL," +
                        "voteAverage REAL NOT NULL," +
                        "timestamp INTEGER NOT NULL," +
                        "note TEXT NOT NULL" +
                        ")")
            }

        }
        private val HISTORY_MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE HistoryEntity")
                database.execSQL("CREATE TABLE HistoryEntity (" +
                        "id TEXT PRIMARY KEY NOT NULL," +
                        "movieId INTEGER NOT NULL," +
                        "title TEXT NOT NULL," +
                        "releasedYear TEXT NOT NULL," +
                        "voteAverage REAL NOT NULL," +
                        "timestamp INTEGER NOT NULL," +
                        "note TEXT NOT NULL" +
                        ")")
            }

        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}