package com.example.android.moviefinder.app

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.moviefinder.model.local.HistoryDao
import com.example.android.moviefinder.model.local.HistoryDatabase

class App : Application() {

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDatabase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDatabase::class.java) {
                    if (appInstance == null)
                        throw IllegalStateException("Application is null while creating DataBase")
                    db = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        HistoryDatabase::class.java,
                        DB_NAME
                    ).addMigrations(MIGRATION_1_2).build()
                }
            }

            return db!!.historyDao()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
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
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}