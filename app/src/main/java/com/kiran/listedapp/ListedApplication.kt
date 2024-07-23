package com.kiran.listedapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ListedApplication : Application() {
    companion object {
        private const val PREFERENCE_NAME = "com.kiran.listedApp.PREFERENCE_FILE_KEY"
        private const val API_READ_ACCESS_TOKEN_KEY = "API_READ_ACCESS_TOKEN"
        private const val ACCESS_TOKEN_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
    }

    override fun onCreate() {
        super.onCreate()
        val token = retrieveTokenFromBackend()
        saveToken(token)
    }

    private fun retrieveTokenFromBackend(): String {
        return ACCESS_TOKEN_KEY
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(API_READ_ACCESS_TOKEN_KEY, token).apply()
    }
}