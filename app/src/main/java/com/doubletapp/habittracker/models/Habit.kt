package com.doubletapp.habittracker.models

data class Habit(
    var id: Int?,
    var title: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var countComplete: Int,
    var period: Int,
    var color: Int,
    var uid: String? = null
)