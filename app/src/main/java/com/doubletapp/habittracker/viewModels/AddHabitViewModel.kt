package com.doubletapp.habittracker.viewModels

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.models.HabitsDatabase
import com.doubletapp.habittracker.util.toMutableLiveData

class AddHabitViewModel(
    habitId: Int?
): ViewModel() {
    val habit: MutableLiveData<Habit> = habitId?.let {
        Settings.dbDao?.findHabitById(it)?.toMutableLiveData()
    } ?: MutableLiveData<Habit>()
    var habitType = HabitType.NONE
    var habitColor = Color.WHITE

    fun uploadHabit(
        title: String,
        description: String,
        priority: String,
        countComplete: Int,
        period: Int
    ) {
        habit.value?.let {
            it.title = title
            it.description = description
            it.priority = priority
            it.type = habitType
            it.countComplete = countComplete
            it.period = period
            it.color = habitColor
            Settings.dbDao?.update(it)
        } ?: run {
            Settings.dbDao?.insert(
                Habit(
                    title,
                    description,
                    priority,
                    habitType,
                    countComplete,
                    period,
                    habitColor
                )
            )
        }
    }
}