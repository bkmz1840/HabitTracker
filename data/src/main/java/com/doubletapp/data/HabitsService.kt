package com.doubletapp.data

import com.doubletapp.data.models.CreateEditResponse
import com.doubletapp.data.models.Habit
import com.doubletapp.data.models.HabitCompleteResponse
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
    ): CreateEditResponse

    @FormUrlEncoded
    @POST("habit_done")
    suspend fun submitCompleteHabit(
        @Header("Authorization") token: String,
        @Field("date") data: Int,
        @Field("habit_uid") uid: String,
    ): HabitCompleteResponse
}