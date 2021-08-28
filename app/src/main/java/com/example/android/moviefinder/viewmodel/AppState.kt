package com.example.android.moviefinder.viewmodel

import com.example.android.moviefinder.model.Movie

sealed class AppState {
    data class Success(val movies: ArrayList<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
