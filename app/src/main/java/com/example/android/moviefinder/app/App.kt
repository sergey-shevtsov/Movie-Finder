package com.example.android.moviefinder.app

import android.app.Application
import androidx.room.Room
import com.example.android.moviefinder.model.local.FavoritesDao
import com.example.android.moviefinder.model.local.FavoritesDatabase
import java.lang.IllegalStateException

class App : Application() {

    companion object {
        private var appInstance: App? = null
        private var db: FavoritesDatabase? = null
        private const val DB_NAME = "Favorites.db"

        fun getFavoritesDao(): FavoritesDao {
            if (db == null) {
                synchronized(FavoritesDatabase::class.java) {
                    if (appInstance == null)
                        throw IllegalStateException("Application is null while creating DataBase")
                    db = Room.databaseBuilder(
                        appInstance!!.applicationContext,
                        FavoritesDatabase::class.java,
                        DB_NAME).build()
                }
            }

            return db!!.favoritesDao()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}