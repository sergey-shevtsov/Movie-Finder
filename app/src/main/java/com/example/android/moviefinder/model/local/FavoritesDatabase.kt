package com.example.android.moviefinder.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoritesEntity::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoritesDao() : FavoritesDao

}