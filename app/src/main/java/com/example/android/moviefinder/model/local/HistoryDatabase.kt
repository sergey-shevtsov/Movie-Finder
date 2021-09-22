package com.example.android.moviefinder.model.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [HistoryEntity::class],
    exportSchema = true
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}