package com.example.android.moviefinder.viewmodel

sealed class AppState {
    object Loading : AppState()
    data class Success<T>(val data: T) : AppState()
    data class Error(val t: Throwable) : AppState()
}
