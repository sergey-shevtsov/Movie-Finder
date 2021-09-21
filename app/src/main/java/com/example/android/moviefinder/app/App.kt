package com.example.android.moviefinder.app

import android.app.Application
import androidx.room.Room
import java.lang.IllegalStateException

class App : Application() {

    companion object {
        private var appInstance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

}