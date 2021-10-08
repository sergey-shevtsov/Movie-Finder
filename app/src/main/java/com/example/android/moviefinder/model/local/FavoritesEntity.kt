package com.example.android.moviefinder.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val movieId: Int,
    val posterPath: String,
    val title: String,
    val releasedYear: String,
    val voteAverage: Double,
    val timestamp: Long
)
