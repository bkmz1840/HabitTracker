package com.doubletapp.data

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RepeatFailedRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var response = chain.proceed(originalRequest)
        while (!response.isSuccessful) {
            Log.e("LOG_ERROR_HTTP_TAG", response.toString())
            Thread.sleep(1_000)
            response = chain.proceed(originalRequest)
        }
        return response
    }
}