package com.example.android.moviefinder.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class HistoryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val movieId: Int,
    val title: String,
    val releasedYear: String,
    val voteAverage: Double,
    val timestamp: Long,
    var note: String
) : Parcelable
