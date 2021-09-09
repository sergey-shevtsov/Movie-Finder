package com.example.android.moviefinder.model

interface Repository {
    fun getRecommendedMoviesFromLocalStorage(): List<Movie>
    fun getRecommendedMoviesFromServer(): List<Movie>
    fun getTopRatedMoviesFromLocalStorage(): List<Movie>
    fun getTopRatedMoviesFromServer(): List<Movie>
    fun getComedyMoviesFromLocalStorage(): List<Movie>
    fun getComedyMoviesFromServer(): List<Movie>
    fun getMovieByIdFromLocalStorage(id: Int): Movie?
    fun getMovieByIdFromServer(id: Int): Movie?
}