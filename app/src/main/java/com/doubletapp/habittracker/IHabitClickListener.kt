package com.doubletapp.habittracker

import com.doubletapp.habittracker.adapters.HabitsAdapter

interface IHabitClickListener {
    fun onHabitClick(position: Int)
}