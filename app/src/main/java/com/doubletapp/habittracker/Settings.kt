package com.doubletapp.habittracker

import com.doubletapp.habittracker.models.IHabitDao

class Settings {
    companion object {
        const val KEY_EDIT_HABIT_ID = "ID_HABIT_TO_EDIT"
        var dbDao: IHabitDao? = null
    }
}