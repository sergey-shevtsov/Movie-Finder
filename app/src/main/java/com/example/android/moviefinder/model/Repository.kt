package com.example.android.moviefinder.model

interface Repository {
    fun getMoviesFromLocalStorage(): List<Movie>
    fun getMoviesFromServer(): List<Movie>
    fun getMovieByIdFromLocalStorage(id: Int): Movie?
    fun getMovieByIdFromServer(id: Int): Movie?
}