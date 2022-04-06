package com.doubletapp.habittracker.viewModels

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitType

class AddHabitViewModel(
    private var habit: Habit?
): ViewModel() {
    var habitType = habit?.type ?: HabitType.NONE
    var habitColor = Color.WHITE

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
            HabitList.updateHabit(it, it.id)
        } ?: run {
            val id = HabitList.lastId + 1
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
            habit?.let{ HabitList.addHabit(it) }
        }
    }
}