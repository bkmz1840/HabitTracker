package com.doubletapp.data

import com.doubletapp.data.models.Habit

fun List<Habit>.getNonServiceHabits(serviceHabits: List<Habit>): List<Habit> {
    val mapServiceHabits = serviceHabits.associateBy { it.uid }
    return this.filter { it.uid == null || mapServiceHabits[it.uid] != it }
}

fun Habit.toDomain(): com.doubletapp.domain.models.Habit = com.doubletapp.domain.models.Habit(
    this.id,
    this.title,
    this.description,
    this.priority,
    this.type,
    this.countComplete,
    this.currentComplete,
    this.period.toLong() * 1000L,
    this.color,
    this.uid
)

fun com.doubletapp.domain.models.Habit.fromDomain(): Habit {
    val habit = Habit(
        this.title,
        this.description,
        this.priority,
        this.type,
        this.countComplete,
        this.currentComplete,
        (this.period / 1000).toInt(),
        this.color,
        this.uid
    )
    habit.id = this.id
    return habit
}

fun List<Habit>.toDomain(): List<com.doubletapp.domain.models.Habit> = this.map { it.toDomain() }