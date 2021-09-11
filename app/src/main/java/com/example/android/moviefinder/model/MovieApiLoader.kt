package com.example.android.moviefinder.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.example.android.moviefinder.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

sealed class MovieApiLoader() {

    class MovieListLoader(private val listener: MovieListLoaderListener) : MovieApiLoader() {
        @RequiresApi(Build.VERSION_CODES.N)
        fun getMovieList(language: String = "en-US", request: String, page: Int = 1) {
            Thread {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    listener.onLoading()
                }
                val uri =
                    URL("https://api.themoviedb.org/3/movie/$request?api_key=${BuildConfig.TMDB_API_KEY}&language=$language&page=$page")
                var urlConnection: HttpsURLConnection? = null

                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                    }

                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val result = reader.lines().collect(Collectors.joining("\n"))
                    val movieListDTO = Gson().fromJson(result, MovieListDTO::class.java)

                    handler.post {
                        listener.onLoaded(movieListDTO)
                    }
                } catch (e: Exception) {
                    handler.post {
                        listener.onFailed(e)
                    }
                } finally {
                    urlConnection?.disconnect()
                }
            }.start()
        }

        interface MovieListLoaderListener {
            fun onLoading()
            fun onLoaded(movieListDTO: MovieListDTO)
            fun onFailed(throwable: Throwable)
        }
    }

    class MovieLoader(private val listener: MovieLoaderListener) : MovieApiLoader() {
        @RequiresApi(Build.VERSION_CODES.N)
        fun getMovieById(language: String = "en-US", id: Int) {
            Thread {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    listener.onLoading()
                }
                val uri =
                    URL("https://api.themoviedb.org/3/movie/$id?api_key=${BuildConfig.TMDB_API_KEY}&language=$language")
                var urlConnection: HttpsURLConnection? = null

                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                    }

                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val result = reader.lines().collect(Collectors.joining("\n"))
                    val movieDTO = Gson().fromJson(result, MovieDTO::class.java)

                    handler.post {
                        listener.onLoaded(movieDTO)
                    }
                } catch (e: Exception) {
                    handler.post {
                        listener.onFailed(e)
                    }
                } finally {
                    urlConnection?.disconnect()
                }
            }.start()
        }

        interface MovieLoaderListener {
            fun onLoading()
            fun onLoaded(movieDTO: MovieDTO)
            fun onFailed(throwable: Throwable)
        }
    }
}