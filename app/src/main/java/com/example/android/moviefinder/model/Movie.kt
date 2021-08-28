package com.example.android.moviefinder.model

import com.example.android.moviefinder.R

data class Movie(
    val title: String = "Terminator",
    val imageId: Int = R.drawable.dummy,
    val released: Int = 1984,
    val rating: Float = 8.0f)
