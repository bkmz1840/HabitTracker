package com.doubletapp.habittracker.viewModels

import androidx.lifecycle.*
import com.doubletapp.domain.HabitsUseCases
import com.doubletapp.habittracker.models.Habit
import com.doubletapp.habittracker.util.fromDomain
import com.doubletapp.habittracker.util.toDomain
import com.doubletapp.habittracker.util.toMutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitListViewModel(private val useCases: HabitsUseCases): ViewModel() {
    private val searchString = MutableLiveData<String>()
    private val _habits: MutableLiveData<List<Habit>> = Transformations.switchMap(searchString) {
        liveData { emit(loadHabits(it)) }
    }.toMutableLiveData()
    val habits: LiveData<List<Habit>> = _habits

    private val _progressStatus: MutableLiveData<Boolean> = MutableLiveData()
    val progressStatus: LiveData<Boolean> = _progressStatus

    init {
        searchString.value = ""
    }

    private suspend fun loadHabits(title: String): List<Habit> = withContext(Dispatchers.IO) {
        _progressStatus.postValue(true)
        val habits = useCases.getAllHabits(title).fromDomain()
        _progressStatus.postValue(false)
        habits
    }

    fun searchHabits(title: String) {
        searchString.value = title
    }

    fun sortHabitsByPriority() {
        habits.value?.sortedByDescending{ habit: Habit -> habit.priority.ordinal }?.let {
            _habits.postValue(it)
        }
    }

    fun submitHabitComplete(habit: Habit): Flow<Boolean> = flow {
        emit(useCases.submitHabitComplete(habit.toDomain()))
    }
}

 class HabitListViewModelFactory(private val useCases: HabitsUseCases): ViewModelProvider.Factory {
     override fun <T : ViewModel?> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(HabitListViewModel::class.java)) {
             @Suppress("UNCHECKED_CAST")
             return HabitListViewModel(useCases) as T
         }
         throw IllegalArgumentException("Unknown ViewModel class")
     }
 }