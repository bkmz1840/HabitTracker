package com.doubletapp.data

class Settings {
    companion object {
        const val LOG_ERROR_HTTP_TAG = "HTTP_ERROR"
        const val habitsServiceToken: String = BuildConfig.API_TOKEN
        const val API_BASE_URL: String = BuildConfig.API_BASE_URL
    }
}