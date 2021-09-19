package com.example.android.moviefinder.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("3/movie/{id}")
    fun getMovieDetails(
        @Path("id") id: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailsDTO>


    @GET("3/movie/{category}")
    fun getMovieList(
        @Path("category") category: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieListDTO>
}