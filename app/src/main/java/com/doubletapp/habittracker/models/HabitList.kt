package com.doubletapp.habittracker.models

import android.graphics.Color
import com.doubletapp.habittracker.util.sortByType

interface IHabitsRepo {
    val observers: MutableList<IHabitsRepoListener>

    fun addObserver(observer: IHabitsRepoListener)
    fun removeObserver(observer: IHabitsRepoListener)
    fun notifyObservers()
}

interface IHabitsRepoListener {
    fun updateHabits(habits: HabitList)
}

data class HabitRepoPosition(
    val repoPosition: Int = -1,
    val positionCurrentType: Int = -1,
)

class HabitList(
    private val goodHabits: MutableList<Habit>,
    private val badHabits: MutableList<Habit>,
) {
    private fun addHabit(habit: Habit) {
        when (habit.type) {
            HabitType.GOOD -> {
                goodHabits.add(habit)
                goodHabits.lastIndex
            }
            HabitType.BAD -> {
                badHabits.add(habit)
                badHabits.lastIndex
            }
            else -> throw IllegalArgumentException("Unexpected habit type: ${habit.type}")
        }
    }

    private fun updateHabit(habit: Habit, position: Int) {
        when (habit.type) {
            HabitType.GOOD -> goodHabits[position] = habit
            HabitType.BAD -> badHabits[position] = habit
            else -> throw IllegalArgumentException("Unexpected habit type: ${habit.type}")
        }
    }

    fun getHabit(habitType: HabitType, position: Int): Habit = when (habitType) {
        HabitType.GOOD -> goodHabits[position]
        HabitType.BAD -> badHabits[position]
        else -> throw IllegalArgumentException("Unexpected habit type: $habitType")
    }

    fun getHabitsByType(habitType: HabitType): List<Habit> = when (habitType) {
        HabitType.GOOD -> goodHabits
        HabitType.BAD -> badHabits
        else -> throw IllegalArgumentException("Unexpected habit type: $habitType")
    }

    companion object : IHabitsRepo {
        private val HABITS = mutableListOf(
            Habit(
                1,
                "Test habit",
                "It is test habit. It is created that programmer can test, how recycler view is working",
                "Средний", HabitType.BAD, 5, 10,
                Color.parseColor("#B0E1FC")
            ),
            Habit(
                2,
                "Test habit",
                "It is test habit. It is created that programmer can test, how recycler view is working",
                "Средний", HabitType.GOOD, 5, 10,
                Color.parseColor("#B0E1FC")
            ),
            Habit(
                3,
                "Test habit",
                "It is test habit. It is created that programmer can test, how recycler view is working",
                "Средний", HabitType.BAD, 5, 10,
                Color.parseColor("#B0E1FC")
            )
        )
        private lateinit var habitList: HabitList
        val lastId: Int get() = HABITS.last().id

        private fun getHabitPositionById(id: Int): HabitRepoPosition {
            val typePositions = mutableMapOf(
                HabitType.GOOD to 0,
                HabitType.BAD to 0
            )
            for (i in HABITS.indices) {
                if (HABITS[i].id == id) {
                    val typePosition = typePositions[HABITS[i].type] ?: throw IllegalArgumentException(
                        "Habit with $id has unexpected type: ${HABITS[i].type}"
                    )
                    return HabitRepoPosition(i, typePosition)
                }
                typePositions[HABITS[i].type] = typePositions[HABITS[i].type]?.plus(1)
                    ?: throw IllegalArgumentException("Habit with $id has unexpected type: ${HABITS[i].type}")
            }
            return HabitRepoPosition()
        }

        fun loadHabits(): HabitList {
            habitList = HABITS.sortByType()
            return habitList
        }

        fun addHabit(habit: Habit) {
            HABITS.add(habit)
            habitList.addHabit(habit)
            notifyObservers()
        }

        fun updateHabit(habit: Habit, id: Int) {
            val position = getHabitPositionById(id)
            if (position.repoPosition == -1)
                throw NoSuchElementException("Habit with id $id not found")
            HABITS[position.repoPosition] = habit
            habitList.updateHabit(habit, position.positionCurrentType)
            notifyObservers()
        }

        override val observers: MutableList<IHabitsRepoListener> = mutableListOf()

        override fun addObserver(observer: IHabitsRepoListener) {
            observers.add(observer)
        }

        override fun removeObserver(observer: IHabitsRepoListener) {
            observers.remove(observer)
        }

        override fun notifyObservers() {
            observers.forEach {
                it.updateHabits(habitList)
            }
        }
    }
}