package com.example.android.moviefinder.model

interface Repository {
    fun getMoviesFromLocalStorage(): ArrayList<Movie>
    fun getMoviesFromServer(): ArrayList<Movie>
}