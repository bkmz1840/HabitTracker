package com.doubletapp.habittracker.util

import android.text.Editable
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitType

fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun List<Habit>.sortByType(): List<List<Habit>> {
    val sortedHabits = mapOf(
        HabitType.GOOD to mutableListOf<Habit>(),
        HabitType.BAD to mutableListOf(),
    )
    this.forEach {
        if (it.type == HabitType.NONE)
            throw IllegalArgumentException("Unexpected habit type of habit ${it.title}")
        sortedHabits[it.type]?.add(it)
    }
    return listOf(
        sortedHabits[HabitType.GOOD]?.toList() ?: listOf(),
        sortedHabits[HabitType.BAD]?.toList() ?: listOf(),
    )
}