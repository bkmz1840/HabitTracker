package com.doubletapp.habittracker.viewModels

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.models.HabitsRepo

class AddHabitViewModel(
    habitId: Int?
): ViewModel() {
    var habit: Habit? = null
    var habitType = HabitType.NONE
    var habitColor = Color.WHITE

    init {
        habitId?.let {
            val loadedHabit = HabitsRepo.getHabit(it)
            habit = loadedHabit
            habitType = loadedHabit.type
            habitColor = loadedHabit.color
        }
    }

    fun uploadHabit(
        title: String,
        description: String,
        priority: String,
        countComplete: Int,
        period: Int
    ) {

        habit?.let {
            it.title = title
            it.description = description
            it.priority = priority
            it.type = habitType
            it.countComplete = countComplete
            it.period = period
            it.color = habitColor
            HabitsRepo.updateHabit(it, it.id)
        } ?: run {
            val id = HabitsRepo.lastId + 1
            habit = Habit(
                id,
                title,
                description,
                priority,
                habitType,
                countComplete,
                period,
                habitColor
            )
            habit?.let{ HabitsRepo.addHabit(it) }
        }
    }
}