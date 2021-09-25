package com.example.android.moviefinder.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM FavoritesEntity ORDER BY timestamp DESC")
    fun all(): List<FavoritesEntity>

    @Query("SELECT * FROM FavoritesEntity WHERE movieId IS :movieId")
    fun getByMovieId(movieId: Int): List<FavoritesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM FavoritesEntity WHERE movieId IS :movieId")
    fun deleteByMovieId(movieId: Int)

    @Query("DELETE FROM FavoritesEntity")
    fun clear()
}