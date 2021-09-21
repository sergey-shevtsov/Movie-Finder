package com.example.android.moviefinder.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoritesEntity(
    @PrimaryKey
    val id: Long,
    val posterPath: String,
    val title: String,
    val releaseYear: String,
    val voteAverage: Double,
    val voteCount: Long,
    val timestamp: Long
)
