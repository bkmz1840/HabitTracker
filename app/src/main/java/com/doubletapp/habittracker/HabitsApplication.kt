package com.doubletapp.habittracker

import android.app.Application
import com.doubletapp.habittracker.models.HabitsDatabase
import com.doubletapp.habittracker.models.HabitsRepo

class HabitsApplication: Application() {
    private val database by lazy { HabitsDatabase(this) }
    val repository by lazy { HabitsRepo(database.habitsDao()) }
}