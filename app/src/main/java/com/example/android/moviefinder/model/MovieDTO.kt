package com.example.android.moviefinder.model

data class MovieDTO(
    val budget: Int?,
    val genres: Array<GenresDTO.GenreDTO>?,
    val id: Int?,
    val original_title: String?,
    val overview: String?,
    val release_date: String?,
    val revenue: Int?,
    val runtime: Int?,
    val title: String?,
    val vote_average: Double?,
    val vote_count: Int?
)
