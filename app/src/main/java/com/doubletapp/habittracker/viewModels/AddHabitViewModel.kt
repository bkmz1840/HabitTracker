package com.doubletapp.habittracker.viewModels

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitPriority
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.models.HabitsRepo
import com.doubletapp.habittracker.util.toMutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddHabitViewModel(
    habitId: Int?,
    private val repo: HabitsRepo,
) : ViewModel() {
    val habit: MutableLiveData<Habit> = habitId?.let {
        repo.findHabitById(it).toMutableLiveData()
    } ?: MutableLiveData<Habit>()
    var habitType = HabitType.NONE
    var habitColor = Color.WHITE
    var habitPriority = HabitPriority.NONE
    val validationErrors: MutableLiveData<List<String>> = MutableLiveData()

    fun validateHabit(
        title: String,
        description: String,
        countComplete: Int?,
        period: Int?
    ) {
        val errorFields = mutableListOf<String>()
        if (title.isEmpty()) errorFields.add(Settings.ERROR_FIELD_TITLE)
        if (description.isEmpty()) errorFields.add(Settings.ERROR_FIELD_DESCRIPTION)
        if (habitPriority == HabitPriority.NONE) errorFields.add(Settings.ERROR_FIELD_PRIORITY)
        if (countComplete == null) errorFields.add(Settings.ERROR_FIELD_COUNT_COMPLETE)
        if (period == null) errorFields.add(Settings.ERROR_FIELD_PERIOD)
        if (habitType == HabitType.NONE) errorFields.add(Settings.ERROR_FIELD_TYPE)

        if (errorFields.isNotEmpty()) validationErrors.postValue(errorFields)
        else {
            viewModelScope.launch(Dispatchers.IO) {
                if (countComplete != null && period != null) viewModelScope.launch(Dispatchers.IO) {
                    uploadHabit(title, description, countComplete, period)
                    validationErrors.postValue(listOf())
                }
            }
        }
    }

    private suspend fun uploadHabit(
        title: String,
        description: String,
        countComplete: Int,
        period: Int
    ) {
        habit.value?.let {
            it.title = title
            it.description = description
            it.priority = habitPriority
            it.type = habitType
            it.countComplete = countComplete
            it.period = period
            it.color = habitColor
            withContext(Dispatchers.IO) {
                repo.update(it)
            }
        } ?: run {
            withContext(Dispatchers.IO) {
                repo.insert(
                    Habit(
                        title,
                        description,
                        habitPriority,
                        habitType,
                        countComplete,
                        period,
                        habitColor
                    )
                )
            }
        }
    }
}