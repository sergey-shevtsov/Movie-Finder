package com.example.android.moviefinder.model

import retrofit2.Callback

interface Repository {
    fun getMovieDetailsFromServer(
        id: Int,
        language: String,
        apiKey: String,
        callback: Callback<MovieDetailsDTO>
    )
}