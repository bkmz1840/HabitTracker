package com.doubletapp.habittracker.models

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.util.findHabit
import com.doubletapp.habittracker.util.getNonDbHabits
import com.doubletapp.habittracker.util.getNonServiceHabits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitsRepo(
    private val habitsDao: IHabitDao,
    private val habitsService: IHabitsService
) {
    suspend fun getAllHabits(title: String = ""): List<Habit> = withContext(Dispatchers.IO) {
        val habitsFromDb = habitsDao.getAll(title).toMutableList()
        if (title == "") {
            try {
                val habitsFromService = habitsService.listHabits(Settings.habitsServiceToken)
                val nonDbHabits = habitsFromService.getNonDbHabits(habitsFromDb)
                habitsDao.insert(*nonDbHabits.toTypedArray())
                habitsFromDb += nonDbHabits
                val changedHabits = mutableListOf<Habit>()
                habitsFromDb.getNonServiceHabits().forEach {
                    val response = habitsService.createEditHabit(Settings.habitsServiceToken, it)
                    if (response.isSuccess) {
                        val habit = habitsFromDb.findHabit(it)
                        if (habit != null) {
                            habit.uid = response.uid
                            changedHabits.add(habit)
                        }
                    }
                }
                habitsDao.update(*changedHabits.toTypedArray())
            } catch (exc: Exception) {
                Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
            }
        }
        habitsFromDb
    }

    fun findHabitById(id: Int): LiveData<Habit> = habitsDao.findHabitById(id)

    @WorkerThread
    suspend fun insert(habit: Habit) {
        try {
            withContext(Dispatchers.IO) {
                val response = habitsService.createEditHabit(Settings.habitsServiceToken, habit)
                if (response.isSuccess) {
                    habit.uid = response.uid
                    habitsDao.update(habit)
                }
            }
        } catch (exc: Exception) {
            Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
            habitsDao.insert(habit)
        }
    }

    @WorkerThread
    suspend fun update(habit: Habit) {
        habitsDao.update(habit)
        try {
            habitsService.createEditHabit(Settings.habitsServiceToken, habit)
        } catch (exc: Exception) {
            Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
        }
    }
}