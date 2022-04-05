package com.doubletapp.habittracker.models

class HabitList(
    private val goodHabits: MutableList<Habit>,
    private val badHabits: MutableList<Habit>,
) {
    fun addHabit(habit: Habit) {
        when (habit.type) {
            HabitType.GOOD -> goodHabits.add(habit)
            HabitType.BAD -> badHabits.add(habit)
            else -> throw IllegalArgumentException("Unexpected habit type: ${habit.type}")
        }
    }

    fun getHabitsByType(habitType: HabitType): List<Habit> = when (habitType) {
        HabitType.GOOD -> goodHabits
        HabitType.BAD -> badHabits
        else -> throw IllegalArgumentException("Unexpected habit type: $habitType")
    }

    fun updateHabit(habit: Habit, position: Int) {
        when (habit.type) {
            HabitType.GOOD -> goodHabits[position] = habit
            HabitType.BAD -> badHabits[position] = habit
            else -> throw IllegalArgumentException("Unexpected habit type: ${habit.type}")
        }
    }
}