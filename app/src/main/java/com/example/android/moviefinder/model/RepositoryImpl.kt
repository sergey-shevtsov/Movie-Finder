package com.example.android.moviefinder.model

class RepositoryImpl : Repository {
    override fun getMoviesFromLocalStorage(): ArrayList<Movie> {
        return arrayListOf(
            Movie(),
            Movie(),
            Movie(),
            Movie(),
            Movie(),
            Movie(),
            Movie()
        )
    }

    override fun getMoviesFromServer(): ArrayList<Movie> {
        return getMoviesFromLocalStorage()
    }
}