package com.example.android.moviefinder.viewmodel

class MovieNotFoundException(override val message: String, val id: Int) : Exception() {
}