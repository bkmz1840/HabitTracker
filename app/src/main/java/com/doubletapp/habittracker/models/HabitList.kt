package com.doubletapp.habittracker.models

class HabitList(
    private val goodHabits: MutableList<Habit>,
    private val badHabits: MutableList<Habit>,
) {
    fun getHabit(habitType: HabitType, position: Int): Habit = when (habitType) {
        HabitType.GOOD -> goodHabits[position]
        HabitType.BAD -> badHabits[position]
        else -> throw IllegalArgumentException("Unexpected habit type: $habitType")
    }

    fun getHabitsByType(habitType: HabitType): List<Habit> = when (habitType) {
        HabitType.GOOD -> goodHabits
        HabitType.BAD -> badHabits
        else -> throw IllegalArgumentException("Unexpected habit type: $habitType")
    }
}