package com.doubletapp.domain.interfaces

import com.doubletapp.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface IHabitsRepo {
    suspend fun getAllHabits(title: String = ""): List<Habit>
    suspend fun findHabitById(id: Int): Habit
    suspend fun insertUpdate(habit: Habit)
    suspend fun submitCompleteHabit(habit: Habit): Boolean
}