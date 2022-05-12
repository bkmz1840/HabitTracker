package com.doubletapp.habittracker.models

import android.os.Parcelable
import androidx.room.TypeConverter
import com.doubletapp.habittracker.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType(val resId: Int): Parcelable {
    NONE(-1),
    BAD(R.string.habit_type_bad),
    GOOD(R.string.habit_type_good);

    fun toInt(): Int = when (this) {
        BAD -> 0
        GOOD -> 1
        else -> 0
    }

    companion object {
        fun fromInt(typeInt: Int): HabitType = when (typeInt) {
            0 -> BAD
            1 -> GOOD
            else -> NONE
        }
    }
}

class HabitTypeConverter {
    @TypeConverter
    fun toHabitType(resId: Int): HabitType = when (resId) {
        -1 -> HabitType.NONE
        R.string.habit_type_bad -> HabitType.BAD
        R.string.habit_type_good -> HabitType.GOOD
        else -> HabitType.NONE
    }

    @TypeConverter
    fun fromHabitType(habitType: HabitType): Int = habitType.ordinal
}