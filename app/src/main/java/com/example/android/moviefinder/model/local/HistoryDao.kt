package com.example.android.moviefinder.model.local

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity ORDER BY timestamp DESC")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE movieId IS :movieId")
    fun getHistoryByMovieId(movieId: Int): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historyEntity: HistoryEntity)

    @Update
    fun update(historyEntity: HistoryEntity)

    @Delete
    fun delete(historyEntity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity")
    fun clear()
}