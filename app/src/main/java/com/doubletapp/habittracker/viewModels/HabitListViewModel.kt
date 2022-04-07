package com.doubletapp.habittracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitList
import com.doubletapp.habittracker.models.HabitType
import com.doubletapp.habittracker.models.IHabitsRepoListener

class HabitListViewModel: ViewModel(), IHabitsRepoListener {
    private val _habits = MutableLiveData<HabitList>()
    val habits: LiveData<HabitList> = _habits

    init {
        HabitList.addObserver(this)
        loadHabits()
    }

    private fun getHabits(): HabitList = HabitList.loadHabits()

    private fun loadHabits() {
        _habits.postValue(getHabits())
    }

    fun getHabit(habitType: HabitType, position: Int): Habit =
        _habits.value?.getHabit(habitType, position) ?: throw IllegalArgumentException(
            "Habit not found with type $habitType by position $position"
        )

    fun searchHabits(title: String) {
        val habits = getHabits()
        if (title.isEmpty())
            _habits.postValue(habits)
        else {
            val filteredHabits = habits.filterHabitsByTitle(title)
            _habits.postValue(filteredHabits)
        }
    }

    fun sortHabitsByPriority() {
        val habits = getHabits().sortByPriority()
        _habits.postValue(habits)
    }

    override fun updateHabits(habits: HabitList) {
        _habits.postValue(habits)
    }

    override fun onCleared() {
        HabitList.removeObserver(this)
        super.onCleared()
    }
}