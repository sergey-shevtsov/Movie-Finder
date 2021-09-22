package com.example.android.moviefinder.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.moviefinder.model.MovieDetailsDTO

@Entity
data class HistoryEntity(
    @PrimaryKey
    val id: Int,
    val movie: MovieDetailsDTO,
    val timestamp: Long,
    val note: String
)
