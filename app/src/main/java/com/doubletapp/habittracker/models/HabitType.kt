package com.doubletapp.habittracker.models

enum class HabitType(val stringType: String) {
    BAD("Вредная"),
    NEUTRAL("Нейтральная"),
    GOOD("Хорошая");

    companion object {
        fun fromString(string: String): HabitType {
            val stringToType = mapOf(
                "Вредная" to BAD,
                "Нейтральная" to NEUTRAL,
                "Хорошая" to GOOD
            )
            if (!stringToType.containsKey(string))
                throw IllegalArgumentException("Unexpected string of habit type")
            return stringToType[string] ?: throw NullPointerException("Habit type is null")
        }
    }
}