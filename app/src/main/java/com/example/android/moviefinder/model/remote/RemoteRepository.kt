package com.example.android.moviefinder.model.remote

import com.example.android.moviefinder.model.MovieDetailsDTO
import com.example.android.moviefinder.model.MovieListDTO
import retrofit2.Callback

interface RemoteRepository {
    fun getMovieDetailsFromServer(
        id: Int,
        language: String,
        apiKey: String,
        callback: Callback<MovieDetailsDTO>
    )
    fun getMovieListFromServer(
        category: String,
        language: String,
        page: Int,
        apiKey: String,
        callback: Callback<MovieListDTO>
    )
}