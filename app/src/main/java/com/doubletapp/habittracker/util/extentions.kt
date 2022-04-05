package com.doubletapp.habittracker.util

import android.text.Editable
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitType

fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun List<Habit>.sortByType(): HabitList {
    val sortedHabits = mapOf(
        HabitType.GOOD to mutableListOf<Habit>(),
        HabitType.BAD to mutableListOf(),
    )
    this.forEach {
        if (it.type == HabitType.NONE)
            throw IllegalArgumentException("Unexpected habit type of habit ${it.title}")
        sortedHabits[it.type]?.add(it)
    }
    val goodHabits = sortedHabits[HabitType.GOOD] ?: mutableListOf()
    val badHabits = sortedHabits[HabitType.BAD] ?: mutableListOf()
    return HabitList(goodHabits, badHabits)
}