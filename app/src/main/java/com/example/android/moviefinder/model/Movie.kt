package com.example.android.moviefinder.model

import android.os.Parcelable
import com.example.android.moviefinder.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    val id: Int,
    val imageId: Int,
    val title: String,
    val originalTitle: String,
    val genres: Array<String>,
    val duration: Int,
    val rating: Float,
    val voteCount: Int,
    val budget: Long,
    val revenue: Long,
    val released: String,
    val overview: String
    ) : Parcelable
