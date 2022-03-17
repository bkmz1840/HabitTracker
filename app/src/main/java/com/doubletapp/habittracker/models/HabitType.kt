package com.doubletapp.habittracker.models

import com.doubletapp.habittracker.R

enum class HabitType(val resId: Int) {
    NONE(-1),
    BAD(R.string.habit_type_bad),
    GOOD(R.string.habit_type_good)
}