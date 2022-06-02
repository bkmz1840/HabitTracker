package com.doubletapp.data

import android.content.Context
import com.doubletapp.data.models.*
import com.doubletapp.domain.interfaces.IHabitsRepo
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule(
    private val context: Context,
    private val applicationScope: CoroutineScope
) {
    @Singleton
    @Provides
    fun provideLoadHabitsService(): IHabitsService {
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(RepeatFailedRequestInterceptor())
            .build()
        val gson = GsonBuilder()
            .registerTypeAdapter(
                Habit::class.java,
                HabitJsonSerializer()
            )
            .registerTypeAdapter(
                Habit::class.java,
                HabitJsonDeserializer()
            )
            .create()
        val converterFactory = GsonConverterFactory.create(gson)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
        return retrofit.create(IHabitsService::class.java)
    }

    @Singleton
    @Provides
    fun provideLoadHabitsDatabase(): HabitsDatabase = HabitsDatabase(context)

    @Singleton
    @Provides
    @Named("HABITS_REPO")
    fun provideLoadHabitsRepo(database: HabitsDatabase, apiService: IHabitsService): IHabitsRepo = HabitsRepo(
        database.habitsDao(),
        apiService,
        applicationScope
    )
}