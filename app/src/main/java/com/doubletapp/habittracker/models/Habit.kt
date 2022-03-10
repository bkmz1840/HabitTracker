package com.doubletapp.habittracker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    var title: String,
    var description: String,
    var priority: String,
    var type: HabitType,
    var countComplete: Int,
    var period: Int,
    var color: Int
): Parcelable