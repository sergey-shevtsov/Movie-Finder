package com.example.android.moviefinder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class MovieDetailsDTO(
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
) : Parcelable {
    fun isNotValid(): Boolean {
        return (budget == null
                || genres.isNullOrEmpty()
                || id == null
                || original_title == null
                || overview == null
                || release_date == null
                || revenue == null
                || runtime == null
                || title == null
                || vote_average == null
                || vote_count == null
                )
    }
}
