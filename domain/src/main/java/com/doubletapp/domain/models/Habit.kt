package com.doubletapp.domain.models

data class Habit(
    val id: Int?,
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val countComplete: Int,
    val period: Int,
    val color: Int,
    val uid: String? = null
)