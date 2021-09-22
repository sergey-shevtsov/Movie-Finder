package com.example.android.moviefinder.app

import android.app.Application
import androidx.room.Room
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
                    ).build()
                }
            }

            return db!!.historyDao()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}