package com.doubletapp.habittracker.util

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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
    val goodHabits = sortedHabits[HabitType.GOOD] ?: listOf()
    val badHabits = sortedHabits[HabitType.BAD] ?: listOf()
    return HabitList(goodHabits, badHabits)
}

fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> {
    val mediatorLiveData = MediatorLiveData<T>()
    mediatorLiveData.addSource(this) {
        mediatorLiveData.value = it
    }
    return mediatorLiveData
}

fun List<Habit>.getNonServiceHabits(serviceHabits: List<Habit>): List<Habit> {
    val mapServiceHabits = serviceHabits.associateBy { it.uid }
    return this.filter { it.uid == null || mapServiceHabits[it.uid] != it }
}