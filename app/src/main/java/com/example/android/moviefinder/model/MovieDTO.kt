package com.example.android.moviefinder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MovieDTO(
    val budget: Int?,
    val genres: @RawValue Array<GenresDTO.GenreDTO>?,
    val id: Int?,
    val original_title: String?,
    val overview: String?,
    val release_date: String?,
    val revenue: Int?,
    val runtime: Int?,
    val title: String?,
    val vote_average: Double?,
    val vote_count: Int?
) : Parcelable
