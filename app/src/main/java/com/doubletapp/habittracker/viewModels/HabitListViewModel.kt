package com.doubletapp.habittracker.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.models.IHabitsRepoListener

class HabitListViewModel: ViewModel(), IHabitsRepoListener {
    private val _habits = MutableLiveData<HabitList>()
    val habits = _habits

    init {
        HabitList.addObserver(this)
        loadHabits()
    }

    private fun loadHabits() {
        _habits.postValue(HabitList.loadHabits())
    }

    fun getHabit(habitType: HabitType, position: Int): Habit =
        _habits.value?.getHabit(habitType, position) ?: throw IllegalArgumentException(
            "Habit not found with type $habitType by position $position"
        )

    override fun updateHabits(habits: HabitList) {
        _habits.postValue(habits)
    }

    override fun onCleared() {
        HabitList.removeObserver(this)
        super.onCleared()
    }
}