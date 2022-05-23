package com.doubletapp.habittracker

import android.app.Application
import com.doubletapp.habittracker.models.*
import com.doubletapp.habittracker.util.RepeatFailedRequestInterceptor
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HabitsApplication: Application() {
    private val habitsService = Retrofit.Builder()
        .client(
            OkHttpClient()
                .newBuilder()
                .addInterceptor(RepeatFailedRequestInterceptor())
                .build()
        )
        .baseUrl("https://droid-test-server.doubletapp.ru/api/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
                    .registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
                    .registerTypeAdapter(ApiResponse::class.java, ApiResponseJsonDeserializer())
                    .create()
            )
        )
        .build()
        .create(IHabitsService::class.java)

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { HabitsDatabase(this) }
    val repository by lazy { HabitsRepo(database.habitsDao(), habitsService, applicationScope) }
}