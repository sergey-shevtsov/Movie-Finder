package com.example.android.moviefinder.model

data class MovieListDTO(
    val results: Array<MovieItemDTO>?
) {
    data class MovieItemDTO(
        val overview: String?,
        val release_date: String?,
        val genre_ids: Array<Int>?,
        val id: Int?,
        val title: String?,
        val vote_average: Double?
    )
}
