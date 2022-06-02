package com.doubletapp.domain

import com.doubletapp.domain.interfaces.IHabitsRepo
import com.doubletapp.domain.models.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitsInteractor(private val habitsRepo: IHabitsRepo) {
    suspend fun getAllHabits(title: String = ""): List<Habit> = withContext(Dispatchers.IO) {
        habitsRepo.getAllHabits(title)
    }

    suspend fun findHabitById(id: Int): Habit = withContext(Dispatchers.IO) {
        habitsRepo.findHabitById(id)
    }

    suspend fun insertUpdate(habit: Habit) = habitsRepo.insertUpdate(habit)

    suspend fun submitHabitComplete(habit: Habit): Boolean = withContext(Dispatchers.IO) {
        val result = habitsRepo.submitCompleteHabit(habit)
        if (result) habit.currentComplete += 1
        result
    }
}