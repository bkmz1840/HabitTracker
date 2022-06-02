package com.doubletapp.data

import android.util.Log
import com.doubletapp.domain.models.Habit
import com.doubletapp.domain.interfaces.IHabitsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HabitsRepo(
    private val habitsDao: IHabitDao,
    private val habitsService: IHabitsService,
    private val scope: CoroutineScope
): IHabitsRepo {
    override suspend fun getAllHabits(title: String): List<Habit> = withContext(Dispatchers.IO) {
        var habitsFromDb = habitsDao.getAll(title)
        if (title == "") {
            try {
                habitsFromDb.getNonServiceHabits(habitsService.listHabits(BuildConfig.API_TOKEN)).forEach {
                    habitsService.createEditHabit(BuildConfig.API_TOKEN, it)
                }
                val habitsFromService = habitsService.listHabits(BuildConfig.API_TOKEN)
                habitsDao.deleteAll()
                habitsDao.insert(*habitsFromService.toTypedArray())
                habitsFromDb = habitsDao.getAll()
            } catch (exc: Exception) {
                Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
            }
        }
        habitsFromDb.toDomain()
    }

    override suspend fun findHabitById(id: Int): Habit = withContext(Dispatchers.IO) {
        habitsDao.findHabitById(id).toDomain()
    }

    override suspend fun insertUpdate(habit: Habit) {
        scope.launch(Dispatchers.IO) {
            val convertedHabit = habit.fromDomain()
            try {
                val response = habitsService.createEditHabit(BuildConfig.API_TOKEN, convertedHabit)
                if (response.isSuccess) {
                    convertedHabit.uid = response.uid
                    habitsDao.insert(convertedHabit)
                }
            } catch (exc: Exception) {
                Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
                habitsDao.insert(convertedHabit)
            }
        }
    }

    override suspend fun submitCompleteHabit(habit: Habit): Boolean = withContext(Dispatchers.IO) {
        habit.uid?.let {
            try {
                val date = Date().time.toInt()
                val res = habitsService.submitCompleteHabit(BuildConfig.API_TOKEN, date, it)
                if (res.isSuccess) {
                    val convertedHabit = habit.fromDomain()
                    convertedHabit.currentComplete += 1
                    habitsDao.insert(convertedHabit)
                }
                return@withContext res.isSuccess
            } catch (exc: Exception) {
                Log.e(Settings.LOG_ERROR_HTTP_TAG, exc.toString())
                return@withContext false
            }
        } ?: run { false }
    }
}