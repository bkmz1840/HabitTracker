package com.doubletapp.domain.models

enum class HabitPriority(val priority: Int) {
    NONE(-1),
    LOW(0),
    NEUTRAL(1),
    HIGH(2);

    fun toInt(): Int = this.ordinal

    companion object {
        fun fromInt(priorityInt: Int): HabitPriority = when (priorityInt) {
            0 -> LOW
            1 -> NEUTRAL
            2 -> HIGH
            else -> NONE
        }
    }
}