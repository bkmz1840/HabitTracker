package com.doubletapp.habittracker.models

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class HabitsRepo(private val habitsDao: IHabitDao) {
    fun getAllHabits(title: String = ""): LiveData<List<Habit>> = habitsDao.getAll(title)
    fun findHabitById(id: Int): LiveData<Habit> = habitsDao.findHabitById(id)

    @WorkerThread
    suspend fun insert(habit: Habit) {
        habitsDao.insert(habit)
    }

    @WorkerThread
    suspend fun update(habit: Habit) {
        habitsDao.update(habit)
    }
}