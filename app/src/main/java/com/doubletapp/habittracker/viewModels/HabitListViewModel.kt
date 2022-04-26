package com.doubletapp.habittracker.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.doubletapp.habittracker.Settings
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.util.toMutableLiveData
import java.util.*

class HabitListViewModel: ViewModel() {
    private val searchString = MutableLiveData<String>()
    private val _habits: MutableLiveData<List<Habit>> = Transformations.switchMap(searchString) {
        Settings.dbDao?.getAll(it)
    }.toMutableLiveData()
    val habits: LiveData<List<Habit>> = _habits

    init {
        searchString.value = ""
    }

    fun searchHabits(title: String) {
        searchString.value = title
    }

    fun sortHabitsByPriority() {
        val sortFunc = { habit: Habit ->
            when (habit.priority) {
                "Низкий" -> -1
                "Средний" -> 0
                "Высокий" -> 1
                else -> throw IllegalArgumentException("Unexpected habit priority ${habit.priority}")
            }
        }
        habits.value?.sortedByDescending(sortFunc)?.let {
            _habits.postValue(it)
        }
    }
}