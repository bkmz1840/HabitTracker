package com.doubletapp.habittracker.models

import androidx.room.*

@Entity
data class Habit(
    @ColumnInfo var title: String,
    @ColumnInfo var description: String,
    @ColumnInfo var priority: String,
    @TypeConverters(HabitTypeConverter::class) var type: HabitType,
    @ColumnInfo var countComplete: Int,
    @ColumnInfo var period: Int,
    @ColumnInfo var color: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}