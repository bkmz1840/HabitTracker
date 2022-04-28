package com.doubletapp.habittracker.models

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [Habit::class],
    version = 1,
)
abstract class HabitsDatabase: RoomDatabase() {
    abstract fun habitsDao(): IHabitDao

    companion object {
        private var instance: HabitsDatabase? = null

        operator fun invoke(context: Context): HabitsDatabase = instance ?: synchronized(this) {
            val dbInstance = Room.databaseBuilder(
                context,
                HabitsDatabase::class.java,
                "HabitsDb"
            ).build()
            instance = dbInstance
            dbInstance
        }
    }
}