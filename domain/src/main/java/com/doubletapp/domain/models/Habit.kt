package com.doubletapp.domain.models

data class Habit(
    val id: Int?,
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val countComplete: Int,
    var currentComplete: Int,
    val period: Long,
    val color: Int,
    val uid: String? = null
)