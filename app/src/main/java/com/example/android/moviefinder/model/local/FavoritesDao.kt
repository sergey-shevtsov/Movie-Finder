package com.example.android.moviefinder.model.local

import androidx.room.*

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM FavoritesEntity ORDER BY timestamp")
    fun all(): List<FavoritesEntity>

    @Insert(onConflict = (OnConflictStrategy.REPLACE))
    fun insert(favoritesEntity: FavoritesEntity)

    @Delete
    fun delete(favoritesEntity: FavoritesEntity)

}