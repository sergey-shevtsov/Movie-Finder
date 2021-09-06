package com.example.android.moviefinder.model

import com.example.android.moviefinder.R

data class Movie(
    val id: Int = 0,
    val imageId: Int = R.drawable.dummy,
    val title: String = "Терминатор",
    val originalTitle: String = "Terminator",
    val genres: Array<String> = arrayOf("фантастика", "боевик", "триллер"),
    val duration: Int = 108,
    val rating: Float = 8.0f,
    val voteCount: Int = 10000,
    val budget: Long = 6_400_000,
    val revenue: Long = 78_371_200,
    val released: Int = 1984,
    val overview: String = "История противостояния солдата Кайла Риза и киборга-терминатора, прибывших в 1984-й год из пост-апокалиптического будущего, где миром правят машины-убийцы, а человечество находится на грани вымирания. Цель киборга: убить девушку по имени Сара Коннор, чей ещё нерождённый сын к 2029 году выиграет войну человечества с машинами. Цель Риза: спасти Сару и остановить Терминатора любой ценой."
)
