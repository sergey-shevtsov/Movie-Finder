package com.example.android.moviefinder.model

data class GenresDTO(
    val genres: Array<GenreDTO>?
) {
    data class GenreDTO(
        val id: Int?,
        val name: String?
    )
}
