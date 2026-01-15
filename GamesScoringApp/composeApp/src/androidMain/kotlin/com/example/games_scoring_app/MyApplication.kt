package com.example.games_scoring_app

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // This is the critical line that fixes the "Context not initialized" error
        AppContext.set(this)
    }
}