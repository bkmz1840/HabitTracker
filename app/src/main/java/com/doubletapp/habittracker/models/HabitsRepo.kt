package com.doubletapp.habittracker.models

import android.graphics.Color
import com.doubletapp.habittracker.util.sortByType
import java.util.*
import kotlin.NoSuchElementException

object HabitsRepo {
    private val HABITS = mutableListOf(
        Habit(
            1,
            "First test habit",
            "It is test habit. It is created that programmer can test, how recycler view is working",
            "Низкий", HabitType.BAD, 5, 10,
            Color.parseColor("#B0E1FC")
        ),
        Habit(
            2,
            "Second test habit",
            "It is test habit. It is created that programmer can test, how recycler view is working",
            "Средний", HabitType.GOOD, 5, 10,
            Color.parseColor("#B0E1FC")
        ),
        Habit(
            3,
            "Third test habit",
            "It is test habit. It is created that programmer can test, how recycler view is working",
            "Высокий", HabitType.BAD, 5, 10,
            Color.parseColor("#B0E1FC")
        ),
        Habit(
            4,
            "First another test habit",
            "It is test habit. It is created that programmer can test, how recycler view is working",
            "Высокий", HabitType.GOOD, 5, 10,
            Color.parseColor("#B0E1FC")
        )
    )
    val lastId: Int get() = HABITS.last().id

    private fun getHabitPositionById(id: Int): Int {
        for (i in HABITS.indices) {
            if (HABITS[i].id == id)
                return i
        }
        return -1
    }

    fun loadHabits(): HabitList = HABITS.sortByType()

    fun getHabit(id: Int): Habit {
        val position = getHabitPositionById(id)
        if (position == -1)
            throw NoSuchElementException("Habit with id $id not found")
        return HABITS[position]
    }

    fun addHabit(habit: Habit) {
        HABITS.add(habit)
    }

    fun updateHabit(habit: Habit, id: Int) {
        val position = getHabitPositionById(id)
        if (position == -1)
            throw NoSuchElementException("Habit with id $id not found")
        HABITS[position] = habit
    }

    fun sortByPriority(): HabitList {
        val sortFunc = { habit: Habit ->
            when (habit.priority) {
                "Низкий" -> -1
                "Средний" -> 0
                "Высокий" -> 1
                else -> throw IllegalArgumentException("Unexpected habit prioryty ${habit.priority}")
            }
        }
        val sortedHabits = HABITS.sortedByDescending(sortFunc)
        return sortedHabits.sortByType()
    }

    fun filterHabitsByTitle(title: String): HabitList {
        val filter = { habit: Habit ->
            habit.title.contains(title.lowercase(Locale.getDefault()), true)
        }
        val filteredHabits = HABITS.filter(filter)
        return filteredHabits.sortByType()
    }
}