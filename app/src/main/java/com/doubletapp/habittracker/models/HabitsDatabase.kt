package com.doubletapp.habittracker.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
abstract class HabitsDatabase: RoomDatabase() {
    abstract fun habitsDao(): IHabitDao

    companion object {
        private val instance: HabitsDatabase? = null

        operator fun invoke(context: Context): HabitsDatabase = instance ?: buildDb(context)

        private fun buildDb(context: Context): HabitsDatabase = Room.databaseBuilder(
            context, HabitsDatabase::class.java, "HabitsDb"
        ).allowMainThreadQueries().build()
    }
}