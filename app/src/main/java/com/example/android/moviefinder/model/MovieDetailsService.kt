package com.example.android.moviefinder.model

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.moviefinder.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MOVIE_DETAILS_INTENT_FILTER = "MovieDetailsIntentFilter"
const val BASE_LOCALE = "en-US"
const val LOCALE_EXTRA = "Locale"
const val ID_EXTRA = "Id"
const val RESULT_EXTRA = "Result"
const val SUCCESS_RESULT = "SuccessResult"
const val MOVIE_DETAILS_EXTRA = "MovieDetails"

class MovieDetailsService(name: String = "MovieDetailsService") : IntentService(name) {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val locale = intent.getStringExtra(LOCALE_EXTRA)
            val id = intent.getIntExtra(ID_EXTRA, -1)

            if (id == -1) {
                onEmptyData()
            } else {
                loadMovieDetails(locale, id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovieDetails(locale: String? = BASE_LOCALE, id: Int) {
        val uri = try {
            URL("https://api.themoviedb.org/3/movie/$id?api_key=${BuildConfig.TMDB_API_KEY}&language=$locale")
        } catch (e: MalformedURLException) {
            onMalformedURL()
            return
        }

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

            onResponse(movieDTO)
        } catch (e: Exception) {
            onErrorResponse(e.message ?: "Unknown Error")
        } finally {
            urlConnection?.disconnect()
        }
    }

    private fun onResponse(movieDTO: MovieDTO?) {
        movieDTO?.let {
            onSuccessResponse(it)
        } ?: onEmptyResponse()
    }

    private fun onSuccessResponse(movieDTO: MovieDTO) {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(MOVIE_DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, SUCCESS_RESULT)
                    .putExtra(MOVIE_DETAILS_EXTRA, movieDTO)
            )
    }

    private fun onEmptyResponse() {
//        TODO("Not yet implemented")
    }

    private fun onErrorResponse(s: String) {
//        TODO("Not yet implemented")
    }

    private fun onMalformedURL() {
//        TODO("Not yet implemented")
    }

    private fun onEmptyData() {
//        TODO("Not yet implemented")
    }
}