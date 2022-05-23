package com.doubletapp.habittracker.models

import android.util.Log
import androidx.lifecycle.LiveData
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.util.getNonServiceHabits
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HabitsRepo(
    private val habitsDao: IHabitDao,
    private val habitsService: IHabitsService,
    private val scope: CoroutineScope
) {
    suspend fun getAllHabits(title: String = ""): List<Habit> = withContext(Dispatchers.IO) {
        var habitsFromDb = habitsDao.getAll(title)
        if (title == "") {
            try {
                habitsFromDb.getNonServiceHabits(habitsService.listHabits(Settings.habitsServiceToken)).forEach {
                    habitsService.createEditHabit(Settings.habitsServiceToken, it)
                }
                val habitsFromService = habitsService.listHabits(Settings.habitsServiceToken)
                habitsDao.deleteAll()
                habitsDao.insert(*habitsFromService.toTypedArray())
                habitsFromDb = habitsDao.getAll()
            } catch (exc: Exception) {
                Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
            }
        }
        habitsFromDb
    }

    fun findHabitById(id: Int): LiveData<Habit> = habitsDao.findHabitById(id)

    suspend fun insertUpdate(habit: Habit) = scope.launch(Dispatchers.IO) {
        try {
            val response = habitsService.createEditHabit(Settings.habitsServiceToken, habit)
            if (response.isSuccess) {
                habit.uid = response.uid
                habitsDao.insert(habit)
            }
        } catch (exc: Exception) {
            Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
            habitsDao.insert(habit)
        }
    }
}