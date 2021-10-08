package com.example.android.moviefinder.model.local

interface FavoritesLocalRepository {

    fun getAllFavorites(): List<FavoritesEntity>

    fun getByMovieId(movieId: Int): List<FavoritesEntity>

    fun insertFavorites(favoritesEntity: FavoritesEntity)

    fun deleteFavoritesByMovieId(movieId: Int)

    fun clearFavorites()

}