package com.doubletapp.habittracker.util

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitPriority
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

fun com.doubletapp.domain.models.Habit.fromDomain(): Habit = Habit(
    this.id,
    this.title,
    this.description,
    HabitPriority.fromDomain(this.priority),
    HabitType.fromDomain(this.type),
    this.countComplete,
    this.currentComplete,
    this.period,
    -1 * this.color,
    this.uid
)

fun List<com.doubletapp.domain.models.Habit>.fromDomain(): List<Habit> = this.map { it.fromDomain() }

fun Habit.toDomain(): com.doubletapp.domain.models.Habit = com.doubletapp.domain.models.Habit(
    this.id,
    this.title,
    this.description,
    this.priority.toDomain(),
    this.type.toDomain(),
    this.countComplete,
    this.currentComplete,
    this.period,
    -1 * this.color,
    this.uid
)