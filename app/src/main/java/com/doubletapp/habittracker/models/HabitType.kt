package com.doubletapp.habittracker.models

import android.os.Parcelable
import com.doubletapp.habittracker.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType(val resId: Int): Parcelable {
    NONE(-1),
    BAD(R.string.habit_type_bad),
    GOOD(R.string.habit_type_good)
}