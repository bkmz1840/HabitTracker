package com.doubletapp.habittracker.models

import androidx.room.TypeConverter
import com.doubletapp.habittracker.R

enum class HabitPriority(val resId: Int) {
    NONE(-1),
    LOW(R.string.habit_priority_low),
    NEUTRAL(R.string.habit_priority_neutral),
    HIGH(R.string.habit_priority_high);

    fun toInt(): Int = when (this) {
        HIGH -> 2
        NEUTRAL -> 1
        LOW -> 0
        else -> 1
    }

    companion object {
        fun fromInt(priorityInt: Int): HabitPriority = when (priorityInt) {
            0 -> LOW
            1 -> NEUTRAL
            2 -> HIGH
            else -> NONE
        }
    }
}

class HabitPriorityConverter {
    @TypeConverter
    fun toHabitPriority(resId: Int): HabitPriority = when (resId) {
        -1 -> HabitPriority.NONE
        R.string.habit_priority_low -> HabitPriority.LOW
        R.string.habit_priority_neutral -> HabitPriority.NEUTRAL
        R.string.habit_priority_high -> HabitPriority.HIGH
        else -> HabitPriority.NONE
    }

    @TypeConverter
    fun fromHabitPriority(habitPriority: HabitPriority): Int = habitPriority.ordinal
}