package com.doubletapp.habittracker.viewModels

import androidx.lifecycle.*
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.models.HabitsRepo
import com.doubletapp.habittracker.util.toMutableLiveData

class HabitListViewModel(private val repo: HabitsRepo): ViewModel() {
    private val searchString = MutableLiveData<String>()
    private val _habits: MutableLiveData<List<Habit>> = Transformations.switchMap(searchString) {
        repo.getAllHabits(it)
    }.toMutableLiveData()
    val habits: LiveData<List<Habit>> = _habits

    init {
        searchString.value = ""
    }

    fun searchHabits(title: String) {
        searchString.value = title
    }

    fun sortHabitsByPriority() {
        habits.value?.sortedByDescending{ habit: Habit -> habit.priority.ordinal }?.let {
            _habits.postValue(it)
        }
    }
}

 class HabitListViewModelFactory(private val repo: HabitsRepo): ViewModelProvider.Factory {
     override fun <T : ViewModel?> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(HabitListViewModel::class.java)) {
             @Suppress("UNCHECKED_CAST")
             return HabitListViewModel(repo) as T
         }
         throw IllegalArgumentException("Unknown ViewModel class")
     }
 }