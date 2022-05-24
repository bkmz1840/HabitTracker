package com.doubletapp.domain.models

enum class HabitType(val type: Int) {
    NONE(-1),
    BAD(0),
    GOOD(1);

    fun toInt(): Int = when (this) {
        BAD -> 0
        GOOD -> 1
        else -> 0
    }

    companion object {
        fun fromInt(typeInt: Int): HabitType = when (typeInt) {
            0 -> BAD
            1 -> GOOD
            else -> NONE
        }
    }
}