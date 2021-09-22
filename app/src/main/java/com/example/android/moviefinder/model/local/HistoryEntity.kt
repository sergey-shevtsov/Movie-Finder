package com.example.android.moviefinder.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey
    val id: Int,
    val movieId: Int,
    val title: String,
    val releasedYear: String,
    val voteAverage: Double,
    val timestamp: Long,
    val note: String
)
