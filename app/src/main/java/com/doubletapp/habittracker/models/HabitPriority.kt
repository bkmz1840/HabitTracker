package com.doubletapp.habittracker.models

import com.doubletapp.habittracker.R

enum class HabitPriority(val resId: Int) {
    NONE(-1),
    LOW(R.string.habit_priority_low),
    NEUTRAL(R.string.habit_priority_neutral),
    HIGH(R.string.habit_priority_high);

    fun toDomain(): com.doubletapp.domain.models.HabitPriority = when (this) {
        HIGH -> com.doubletapp.domain.models.HabitPriority.HIGH
        NEUTRAL -> com.doubletapp.domain.models.HabitPriority.NEUTRAL
        LOW -> com.doubletapp.domain.models.HabitPriority.LOW
        else -> com.doubletapp.domain.models.HabitPriority.NEUTRAL
    }

    companion object {
        fun fromDomain(domainPriority: com.doubletapp.domain.models.HabitPriority): HabitPriority =
            when (domainPriority) {
                com.doubletapp.domain.models.HabitPriority.LOW -> LOW
                com.doubletapp.domain.models.HabitPriority.NEUTRAL -> NEUTRAL
                com.doubletapp.domain.models.HabitPriority.HIGH -> HIGH
                else -> NONE
            }
    }
}