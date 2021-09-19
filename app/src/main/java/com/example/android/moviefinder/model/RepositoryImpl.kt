package com.example.android.moviefinder.model

import retrofit2.Callback

class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository {
    override fun getMovieDetailsFromServer(
        id: Int,
        language: String,
        apiKey: String,
        callback: Callback<MovieDetailsDTO>
    ) {
        remoteDataSource.getMovieDetails(id, language, apiKey, callback)
    }

    override fun getMovieListFromServer(
        category: String,
        language: String,
        page: Int,
        apiKey: String,
        callback: Callback<MovieListDTO>
    ) {
        remoteDataSource.getMovieList(category, language, page, apiKey, callback)
    }
}