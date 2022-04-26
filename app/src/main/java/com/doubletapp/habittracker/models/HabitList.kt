package com.doubletapp.habittracker.models

class HabitList(
    private val goodHabits: List<Habit>,
    private val badHabits: List<Habit>,
) {
    fun getHabitsByType(habitType: HabitType): List<Habit> = when (habitType) {
        HabitType.GOOD -> goodHabits
        HabitType.BAD -> badHabits
        else -> throw IllegalArgumentException("Unexpected habit type: $habitType")
    }
}