package com.example.android.moviefinder.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.android.moviefinder.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MovieListLoader(private val listener: MovieListLoaderListener) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMovies(requestName: String) {
        val uri =
            URL("https://api.themoviedb.org/3/movie/$requestName?api_key=${BuildConfig.TMDB_API_KEY}&language=ru-RU&page=1")
        Thread {
            goToInternet(uri)
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun goToInternet(uri: URL) {
        val handler = Handler(Looper.getMainLooper())
        var urlConnection: HttpsURLConnection? = null

        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }

            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))

            val movieListDTO: MovieListDTO = Gson().fromJson(result, MovieListDTO::class.java)

            handler.post {
                listener.onLoaded(movieListDTO)
            }
        } catch (e: Exception) {
            Log.e("", "Fail URI", e)
            listener.onFailed(e)
        } finally {
            urlConnection?.disconnect()
        }
    }

    interface MovieListLoaderListener {
        fun onLoaded(movieListDTO: MovieListDTO)
        fun onFailed(throwable: Throwable)
    }
}