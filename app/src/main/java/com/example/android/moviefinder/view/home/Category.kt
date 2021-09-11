package com.example.android.moviefinder.view.home

import com.example.android.moviefinder.model.MovieApiLoader

data class Category(
    val title: String,
    val adapter: MoviesAdapter,
    val request: String,
    var loader: MovieApiLoader.MovieListLoader? = null
)