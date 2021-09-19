package com.example.android.moviefinder.model

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    private val moviesApi = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(MoviesApi::class.java)

    fun getMovieDetails(
        id: Int,
        language: String,
        apiKey: String,
        callback: Callback<MovieDetailsDTO>
    ) {
        moviesApi.getMovieDetails(id, language, apiKey).enqueue(callback)
    }

    fun getMovieList(
        category: String,
        language: String,
        page: Int,
        apiKey: String,
        callback: Callback<MovieListDTO>
    ) {
        moviesApi.getMovieList(category, language, page, apiKey).enqueue(callback)
    }
}