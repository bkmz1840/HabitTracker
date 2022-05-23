package com.doubletapp.habittracker.models

import retrofit2.http.*

interface IHabitsService {
    @GET("habit")
    suspend fun listHabits(
        @Header("Authorization") token: String
    ): List<Habit>

    @PUT("habit")
    suspend fun createEditHabit(
        @Header("Authorization") token: String,
        @Body habit: Habit
    ): ApiResponse
}