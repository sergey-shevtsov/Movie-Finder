package com.example.android.moviefinder.view.home

import android.view.View

data class Category(
    val title: String,
    val adapter: MoviesAdapter,
    val request: String,
    val view: View
)